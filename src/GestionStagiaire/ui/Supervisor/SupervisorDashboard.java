package GestionStagiaire.ui.Supervisor;

import GestionStagiaire.models.Internship;
import GestionStagiaire.models.Stagiaire;
import GestionStagiaire.models.Supervisor;
import GestionStagiaire.models.User;
import GestionStagiaire.ui.LoginFrame;
import GestionStagiaireDao.InternshipDAO;
import GestionStagiaireDao.StagiaireDAO;
import GestionStagiaireDao.SupervisorDAO;
import GestionStagiaireDao.UserDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupervisorDashboard extends JFrame {
    private User currentUser;
    private Supervisor currentSupervisor;
    private JTabbedPane tabbedPane;
    private JPanel profilePanel;
    private JPanel internshipsPanel;

    // Profile fields
    private JTextField supFirstNameField, supLastNameField, supDepartmentField, supPositionField, supEmailField;

    // Internships management fields
    private JTable supervisedInternshipsTable;
    private DefaultTableModel supervisedInternshipsTableModel;
    private JTextArea internshipDescriptionArea, currentFeedbackArea;
    private JLabel internshipTitleDetailLabel, stagiaireNameDetailLabel, internshipStatusDetailLabel, currentGradeLabel, reportPathDetailLabel, internshipDatesDetailLabel;
    private JComboBox<String> statusComboBox;
    private JTextField gradeField;
    private JButton updateInternshipButton;

    private InternshipDAO internshipDAO;
    private StagiaireDAO stagiaireDAO;
    private SupervisorDAO supervisorDAO;
    private UserDAO userDAO;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Color primaryColor = new Color(0, 102, 204); // Dark blue
    private Color secondaryColor = new Color(240, 240, 240); // Light gray
    private Color accentColor = new Color(0, 153, 255); // Light blue

    public SupervisorDashboard(User user) {
        this.currentUser = user;
        this.internshipDAO = new InternshipDAO();
        this.stagiaireDAO = new StagiaireDAO();
        this.supervisorDAO = new SupervisorDAO();
        this.userDAO = new UserDAO();

        this.currentSupervisor = supervisorDAO.getSupervisorByUserId(currentUser.getId());

        if (this.currentSupervisor == null) {
            JOptionPane.showMessageDialog(this, "Profil superviseur non trouvé pour cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
            return;
        }

        setTitle("Tableau de Bord Superviseur - " + currentUser.getUsername());
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/icon.png")));
        setIconImage(img.getImage());

        createMenuBar();
        initComponents();
        loadSupervisorProfileData();
        loadSupervisedInternshipsData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(secondaryColor);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Profile Tab
        profilePanel = new JPanel(new BorderLayout(10, 10));
        profilePanel.setBorder(new EmptyBorder(10,10,10,10));
        profilePanel.setBackground(secondaryColor);
        createSupervisorProfileTabContent(profilePanel);
        tabbedPane.addTab("Mon Profil", new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/users.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), profilePanel, "Voir mon profil superviseur");

        // Supervised Internships Tab
        internshipsPanel = new JPanel(new BorderLayout(10, 10));
        internshipsPanel.setBorder(new EmptyBorder(10,10,10,10));
        internshipsPanel.setBackground(secondaryColor);
        createSupervisedInternshipsTabContent(internshipsPanel);
        tabbedPane.addTab("Stagiaires Encadrés", new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/interns.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), internshipsPanel, "Gérer les stages des stagiaires encadrés");

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Gestion de Stage - Espace Superviseur");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Bienvenue, " + currentSupervisor.getFirstName() + " " + currentSupervisor.getLastName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        userPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        statusPanel.setBackground(secondaryColor);
        JLabel statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        return statusPanel;
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/logout.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        logoutItem.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/exit.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createSupervisorProfileTabContent(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(15,15,15,15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        supFirstNameField = addSupervisorProfileField(formPanel, gbc, "Prénom:", y++, false);
        supLastNameField = addSupervisorProfileField(formPanel, gbc, "Nom:", y++, false);
        supEmailField = addSupervisorProfileField(formPanel, gbc, "Email:", y++, false);
        supDepartmentField = addSupervisorProfileField(formPanel, gbc, "Département:", y++, true); // Example: Department might be updatable by admin, not supervisor
        supPositionField = addSupervisorProfileField(formPanel, gbc, "Poste:", y++, true);   // Example: Position might be updatable by admin

        // For supervisor, profile update might be less frequent or handled by admin.
        // Add an update button if supervisors are allowed to change some of their info.
        // For this example, we assume profile info is mostly static for supervisors from their dashboard.
        // JButton updateSupProfileButton = createStyledButton("Mettre à jour Profil", primaryColor, Color.WHITE);
        // gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; ...
        // formPanel.add(updateSupProfileButton, gbc);
        // updateSupProfileButton.addActionListener(e -> updateSupervisorProfile());
        
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private JTextField addSupervisorProfileField(JPanel panel, GridBagConstraints gbc, String label, int y, boolean editable) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(25);
        textField.setEditable(editable);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (!editable) {
            textField.setBackground(secondaryColor);
        }
        panel.add(textField, gbc);
        return textField;
    }

    private void loadSupervisorProfileData() {
        supFirstNameField.setText(currentSupervisor.getFirstName());
        supLastNameField.setText(currentSupervisor.getLastName());
        supEmailField.setText(currentUser.getEmail()); // Email from User table
        supDepartmentField.setText(currentSupervisor.getDepartment());
        supPositionField.setText(currentSupervisor.getPosition());
    }
    // private void updateSupervisorProfile() { ... if supervisors can update their own info ... }

    private void createSupervisedInternshipsTabContent(JPanel panel) {
        // Top: List of internships supervised
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Stages que j'encadre"));
        listPanel.setBackground(Color.WHITE);

        supervisedInternshipsTableModel = new DefaultTableModel(new String[]{"ID Stage", "Titre du Stage", "Stagiaire", "Statut", "Début", "Fin"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supervisedInternshipsTable = new JTable(supervisedInternshipsTableModel);
        styleTable(supervisedInternshipsTable);
        supervisedInternshipsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supervisedInternshipsTable.getSelectedRow() != -1) {
                try {
                    loadInternshipDetailsForSupervisor(supervisedInternshipsTable.getSelectedRow());
                } catch (SQLException ex) {
                    Logger.getLogger(SupervisorDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        listPanel.add(new JScrollPane(supervisedInternshipsTable), BorderLayout.CENTER);

        // Bottom: Details and actions for selected internship
        JPanel detailsActionPanel = new JPanel(new GridBagLayout());
        detailsActionPanel.setBorder(BorderFactory.createTitledBorder("Détails et Évaluation du Stage Sélectionné"));
        detailsActionPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,10,5,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;
        internshipTitleDetailLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Titre Stage:", y++);
        stagiaireNameDetailLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Stagiaire:", y++);
        internshipDescriptionArea = addDetailTextAreaSupervisor(detailsActionPanel, gbc, "Description Stage:", y++, 3, false);
        internshipDatesDetailLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Dates Stage:", y++);
        reportPathDetailLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Rapport Soumis:", y++);
        
        // Current Status (display only)
        internshipStatusDetailLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Statut Actuel:", y++);

        // Update Status
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        detailsActionPanel.add(new JLabel("Nouveau Statut:"), gbc);
        statusComboBox = new JComboBox<>(new String[]{"PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"});
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        detailsActionPanel.add(statusComboBox, gbc);
        y++;

        // Current Grade (display only)
        currentGradeLabel = addDetailFieldSupervisor(detailsActionPanel, gbc, "Note Actuelle:", y++);
        
        // Update Grade
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        detailsActionPanel.add(new JLabel("Nouvelle Note (sur 20):"), gbc);
        gradeField = new JTextField(5);
        gradeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        detailsActionPanel.add(gradeField, gbc);
        y++;

        // Feedback
        currentFeedbackArea = addDetailTextAreaSupervisor(detailsActionPanel, gbc, "Feedback:", y++, 4, true);

        updateInternshipButton = createStyledButton("Mettre à Jour Stage (Statut/Note/Feedback)", primaryColor, Color.WHITE);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15,10,5,10);
        detailsActionPanel.add(updateInternshipButton, gbc);
        updateInternshipButton.addActionListener(e -> {
            try {
                updateInternshipBySupervisor();
            } catch (SQLException ex) {
                Logger.getLogger(SupervisorDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listPanel, new JScrollPane(detailsActionPanel));
        splitPane.setResizeWeight(0.45);
        splitPane.setDividerLocation(250);
        panel.add(splitPane, BorderLayout.CENTER);
    }

    private JLabel addDetailFieldSupervisor(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        JLabel valueLabel = new JLabel("-");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(valueLabel, gbc);
        return valueLabel;
    }

    private JTextArea addDetailTextAreaSupervisor(JPanel panel, GridBagConstraints gbc, String label, int y, int rows, boolean editable) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH; gbc.anchor = GridBagConstraints.WEST;
        JTextArea textArea = new JTextArea(rows, 30);
        textArea.setEditable(editable);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (!editable) textArea.setBackground(secondaryColor);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset
        return textArea;
    }

    private void loadSupervisedInternshipsData() {
        supervisedInternshipsTableModel.setRowCount(0);
        List<Internship> internships = internshipDAO.getInternshipsBySupervisorId(currentSupervisor.getId());
        for (Internship internship : internships) {
            Stagiaire stagiaire = stagiaireDAO.getStagiaireById(internship.getStagiaireId());
            String stagiaireName = stagiaire != null ? stagiaire.getFirstName() + " " + stagiaire.getLastName() : "N/A";
            String startDateStr = internship.getStartDate() != null ? dateFormat.format(internship.getStartDate()) : "N/A";
            String endDateStr = internship.getEndDate() != null ? dateFormat.format(internship.getEndDate()) : "N/A";
            supervisedInternshipsTableModel.addRow(new Object[]{
                internship.getId(),
                internship.getTitle(),
                stagiaireName,
                internship.getStatus(),
                startDateStr,
                endDateStr
            });
        }
    }

    private void loadInternshipDetailsForSupervisor(int selectedRowIndex) throws SQLException {
        if (selectedRowIndex < 0) return;
        int internshipId = (int) supervisedInternshipsTableModel.getValueAt(selectedRowIndex, 0);
        Internship selectedInternship = internshipDAO.getInternshipById(internshipId);

        if (selectedInternship != null) {
            Stagiaire stagiaire = stagiaireDAO.getStagiaireById(selectedInternship.getStagiaireId());
            internshipTitleDetailLabel.setText(selectedInternship.getTitle());
            stagiaireNameDetailLabel.setText(stagiaire != null ? stagiaire.getFirstName() + " " + stagiaire.getLastName() + " (CIN: "+stagiaire.getCin()+")" : "N/A");
            internshipDescriptionArea.setText(selectedInternship.getDescription() != null ? selectedInternship.getDescription() : "");
            String startDateStr = selectedInternship.getStartDate() != null ? dateFormat.format(selectedInternship.getStartDate()) : "N/A";
            String endDateStr = selectedInternship.getEndDate() != null ? dateFormat.format(selectedInternship.getEndDate()) : "N/A";
            internshipDatesDetailLabel.setText("Du " + startDateStr + " au " + endDateStr);
            reportPathDetailLabel.setText(selectedInternship.getReportFilePath() != null && !selectedInternship.getReportFilePath().isEmpty() ? selectedInternship.getReportFilePath() : "Non soumis ou chemin non disponible");
            
            internshipStatusDetailLabel.setText(selectedInternship.getStatus());
            statusComboBox.setSelectedItem(selectedInternship.getStatus());
            currentGradeLabel.setText(selectedInternship.getGrade() != null ? String.valueOf(selectedInternship.getGrade()) : "Non noté");
            gradeField.setText(selectedInternship.getGrade() != null ? String.valueOf(selectedInternship.getGrade()) : "");
            currentFeedbackArea.setText(selectedInternship.getFeedback() != null ? selectedInternship.getFeedback() : "");
        } else {
            clearInternshipDetailsSupervisor();
        }
    }

    private void clearInternshipDetailsSupervisor() {
        internshipTitleDetailLabel.setText("-");
        stagiaireNameDetailLabel.setText("-");
        internshipDescriptionArea.setText("");
        internshipDatesDetailLabel.setText("-");
        reportPathDetailLabel.setText("-");
        internshipStatusDetailLabel.setText("-");
        statusComboBox.setSelectedIndex(0);
        currentGradeLabel.setText("-");
        gradeField.setText("");
        currentFeedbackArea.setText("");
    }

    private void updateInternshipBySupervisor() throws SQLException {
        int selectedRow = supervisedInternshipsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un stage à mettre à jour.", "Aucun Stage Sélectionné", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int internshipId = (int) supervisedInternshipsTableModel.getValueAt(selectedRow, 0);
        Internship internshipToUpdate = internshipDAO.getInternshipById(internshipId);
        if (internshipToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Erreur: Stage non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newStatus = (String) statusComboBox.getSelectedItem();
        String gradeStr = gradeField.getText();
        String feedback = currentFeedbackArea.getText();
        Double newGrade = null;

        if (gradeStr != null && !gradeStr.trim().isEmpty()) {
            try {
                newGrade = Double.parseDouble(gradeStr);
                if (newGrade < 0 || newGrade > 20) {
                    JOptionPane.showMessageDialog(this, "La note doit être entre 0 et 20.", "Erreur de Validation", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Format de note invalide. Utilisez un nombre.", "Erreur de Validation", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Update the internship object
        internshipToUpdate.setStatus(newStatus);
        internshipToUpdate.setGrade(newGrade); // Can be null if field is empty
        internshipToUpdate.setFeedback(feedback);

        // Call DAO method to update (assuming such a method exists or will be created)
        // For example: internshipDAO.updateInternshipBySupervisor(internshipToUpdate);
        // For now, using a generic update or specific ones if available in InternshipDAO
        boolean success = internshipDAO.updateInternshipEvaluation(internshipId, newStatus, newGrade, feedback);

        if (success) {
            JOptionPane.showMessageDialog(this, "Stage mis à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            loadSupervisedInternshipsData(); // Refresh table
            if (supervisedInternshipsTable.getRowCount() > selectedRow) {
                 supervisedInternshipsTable.setRowSelectionInterval(selectedRow, selectedRow); // Re-select
            }
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la mise à jour du stage.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 18, 8, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent evt) { button.setBackground(bgColor); }
        });
        return button;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(220,220,220));
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(primaryColor);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID Stage
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Statut
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Début
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Fin
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Titre
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Stagiaire
    }

    public static void main(String[] args) {
        User testUser = new User();
        // IMPORTANT: Replace with an ACTUAL user_id of a SUPERVISOR in your DB
        // And ensure this supervisor has some internships assigned to them.
        testUser.setId(4); // <<<< EXAMPLE ID, CHANGE THIS 
        testUser.setUsername("test.supervisor"); // Replace with actual username
        testUser.setRole("SUPERVISOR");
        testUser.setEmail("supervisor@example.com");

        SwingUtilities.invokeLater(() -> {
            UserDAO uDao = new UserDAO();
            User fetchedUser = uDao.getUserById(testUser.getId());
            if(fetchedUser != null && "SUPERVISOR".equalsIgnoreCase(fetchedUser.getRole())){
                SupervisorDashboard dashboard = new SupervisorDashboard(fetchedUser);
                dashboard.setVisible(true);
            } else {
                System.err.println("Utilisateur de test superviseur non trouvé ou rôle incorrect. Vérifiez l'ID: " + testUser.getId());
                new LoginFrame().setVisible(true);
            }
        });
    }
}

