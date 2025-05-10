package GestionStagiaire.ui.Stagiaire;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StagiaireDashboard extends JFrame {
    private User currentUser;
    private Stagiaire currentStagiaire;
    private JTabbedPane tabbedPane;
    private JPanel profilePanel;
    private JPanel internshipsPanel;

    private JTextField firstNameField, lastNameField, cinField, dobField, emailField, universityField, fieldOfStudyField, progStartDateField, progEndDateField;
    private JTable internshipsTable;
    private DefaultTableModel internshipsTableModel;
    private JTextArea descriptionArea, feedbackArea;
    private JLabel internshipTitleLabel, supervisorLabel, statusLabel, gradeLabel, reportPathLabel, internshipStartDateLabel, internshipEndDateLabel;
    private JButton submitReportButton, browseReportButton;
    private JTextField reportPathTextField;
    private InternshipDAO internshipDAO;
    private StagiaireDAO stagiaireDAO;
    private SupervisorDAO supervisorDAO;
    private UserDAO userDAO;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Color primaryColor = new Color(0, 102, 204); // Dark blue from AdminDashboard
    private Color secondaryColor = new Color(240, 240, 240); // Light gray
    private Color accentColor = new Color(0, 153, 255); // Light blue

    public StagiaireDashboard(User user) throws SQLException {
        this.currentUser = user;
        this.internshipDAO = new InternshipDAO();
        this.stagiaireDAO = new StagiaireDAO();
        this.supervisorDAO = new SupervisorDAO();
        this.userDAO = new UserDAO(); // For fetching user details if needed

        // Fetch Stagiaire details
        this.currentStagiaire = stagiaireDAO.getStagiaireByUserId(currentUser.getId());

        if (this.currentStagiaire == null) {
            JOptionPane.showMessageDialog(this, "Profil stagiaire non trouvé pour cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            // Fallback to login or close
            new LoginFrame().setVisible(true);
            dispose();
            return;
        }

        setTitle("Tableau de Bord Stagiaire - " + currentUser.getUsername());
        setSize(1000, 700);
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
        loadProfileData();
        loadInternshipsData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(secondaryColor);

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Profile Tab
        profilePanel = new JPanel(new BorderLayout(10,10));
        profilePanel.setBorder(new EmptyBorder(10,10,10,10));
        profilePanel.setBackground(secondaryColor);
        createProfileTabContent(profilePanel);
        tabbedPane.addTab("Mon Profil", new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/users.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), profilePanel, "Voir et modifier mon profil");

        // Internships Tab
        internshipsPanel = new JPanel(new BorderLayout(10,10));
        internshipsPanel.setBorder(new EmptyBorder(10,10,10,10));
        internshipsPanel.setBackground(secondaryColor);
        createInternshipsTabContent(internshipsPanel);
        tabbedPane.addTab("Mes Stages", new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/internships.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), internshipsPanel, "Gérer mes stages");

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Gestion de Stage - Espace Stagiaire");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Bienvenue, " + currentStagiaire.getFirstName() + " " + currentStagiaire.getLastName());
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

    private void createProfileTabContent(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(15,15,15,15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields: Prénom, Nom, CIN, Date Naissance (Non-editable)
        // Email (Non-editable from User table)
        // Université, Domaine d'études (Editable)
        // Date début programme, Date fin programme (Non-editable from Stagiaire table)

        int y = 0;
        firstNameField = addProfileField(formPanel, gbc, "Prénom:", y++, false);
        lastNameField = addProfileField(formPanel, gbc, "Nom:", y++, false);
        cinField = addProfileField(formPanel, gbc, "CIN:", y++, false);
        dobField = addProfileField(formPanel, gbc, "Date de Naissance:", y++, false);
        emailField = addProfileField(formPanel, gbc, "Email:", y++, false);
        universityField = addProfileField(formPanel, gbc, "Université:", y++, true);
        fieldOfStudyField = addProfileField(formPanel, gbc, "Domaine d'études:", y++, true);
        progStartDateField = addProfileField(formPanel, gbc, "Début Programme:", y++, false);
        progEndDateField = addProfileField(formPanel, gbc, "Fin Programme:", y++, false);

        JButton updateProfileButton = createStyledButton("Mettre à jour le Profil", primaryColor, Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20,5,5,5);
        formPanel.add(updateProfileButton, gbc);

        updateProfileButton.addActionListener(e -> updateProfile());
        
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private JTextField addProfileField(JPanel panel, GridBagConstraints gbc, String label, int y, boolean editable) {
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
            textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(), 
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));
        }
        panel.add(textField, gbc);
        return textField;
    }

    private void loadProfileData() {
        firstNameField.setText(currentStagiaire.getFirstName());
        lastNameField.setText(currentStagiaire.getLastName());
        cinField.setText(currentStagiaire.getCin());
        dobField.setText(currentStagiaire.getDateOfBirth() != null ? dateFormat.format(currentStagiaire.getDateOfBirth()) : "");
        emailField.setText(currentUser.getEmail());
        universityField.setText(currentStagiaire.getUniversity());
        fieldOfStudyField.setText(currentStagiaire.getFieldOfStudy());
        progStartDateField.setText(currentStagiaire.getStartDate() != null ? dateFormat.format(currentStagiaire.getStartDate()) : "");
        progEndDateField.setText(currentStagiaire.getEndDate() != null ? dateFormat.format(currentStagiaire.getEndDate()) : "");
    }

    private void updateProfile() {
        // Only university and field of study are directly updatable by stagiaire in this example
        currentStagiaire.setUniversity(universityField.getText());
        currentStagiaire.setFieldOfStudy(fieldOfStudyField.getText());

        if (stagiaireDAO.updateStagiaire(currentStagiaire)) {
            JOptionPane.showMessageDialog(this, "Profil mis à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la mise à jour du profil.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createInternshipsTabContent(JPanel panel) {
        // Top: Internships List
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Mes Affectations de Stage"));
        listPanel.setBackground(Color.WHITE);

        internshipsTableModel = new DefaultTableModel(new String[]{"ID", "Titre", "Superviseur", "Statut", "Début", "Fin"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        internshipsTable = new JTable(internshipsTableModel);
        styleTable(internshipsTable);
        internshipsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && internshipsTable.getSelectedRow() != -1) {
                try {
                    loadInternshipDetails(internshipsTable.getSelectedRow());
                } catch (SQLException ex) {
                    Logger.getLogger(StagiaireDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        listPanel.add(new JScrollPane(internshipsTable), BorderLayout.CENTER);

        // Bottom: Internship Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails du Stage Sélectionné"));
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,10,5,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;
        internshipTitleLabel = addDetailField(detailsPanel, gbc, "Titre:", y++);
        descriptionArea = addDetailTextArea(detailsPanel, gbc, "Description:", y++, 3);
        supervisorLabel = addDetailField(detailsPanel, gbc, "Superviseur:", y++);
        statusLabel = addDetailField(detailsPanel, gbc, "Statut:", y++);
        internshipStartDateLabel = addDetailField(detailsPanel, gbc, "Date Début Stage:", y++);
        internshipEndDateLabel = addDetailField(detailsPanel, gbc, "Date Fin Stage:", y++);
        gradeLabel = addDetailField(detailsPanel, gbc, "Note:", y++);
        feedbackArea = addDetailTextArea(detailsPanel, gbc, "Feedback Superviseur:", y++, 2);
        
        // Report Submission Section
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Chemin du Rapport:"), gbc);
        reportPathTextField = new JTextField(30);
        reportPathTextField.setEditable(false);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        detailsPanel.add(reportPathTextField, gbc);
        
        browseReportButton = createStyledButton("Parcourir...", accentColor, Color.WHITE);
        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        detailsPanel.add(browseReportButton, gbc);
        y++;

        submitReportButton = createStyledButton("Soumettre/MàJ Rapport", primaryColor, Color.WHITE);
        gbc.gridx = 1; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        detailsPanel.add(submitReportButton, gbc);

        browseReportButton.addActionListener(e -> browseForReport());
        submitReportButton.addActionListener(e -> {
            try {
                submitReport();
            } catch (SQLException ex) {
                Logger.getLogger(StagiaireDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listPanel, new JScrollPane(detailsPanel));
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(200);
        panel.add(splitPane, BorderLayout.CENTER);
    }

    private JLabel addDetailField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JLabel valueLabel = new JLabel("-");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(valueLabel, gbc);
        return valueLabel;
    }

    private JTextArea addDetailTextArea(JPanel panel, GridBagConstraints gbc, String label, int y, int rows) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH; gbc.anchor = GridBagConstraints.WEST;
        JTextArea textArea = new JTextArea(rows, 30);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setBackground(secondaryColor);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset for next fields
        return textArea;
    }

    private void loadInternshipsData() throws SQLException {
        internshipsTableModel.setRowCount(0); // Clear existing data
        List<Internship> internships = internshipDAO.getInternshipsByStagiaire(currentStagiaire.getId());
        for (Internship internship : internships) {
            Supervisor supervisor = supervisorDAO.getSupervisorById(internship.getSupervisorId());
            String supervisorName = supervisor != null ? supervisor.getFirstName() + " " + supervisor.getLastName() : "N/A";
            String startDateStr = internship.getStartDate() != null ? dateFormat.format(internship.getStartDate()) : "N/A";
            String endDateStr = internship.getEndDate() != null ? dateFormat.format(internship.getEndDate()) : "N/A";
            internshipsTableModel.addRow(new Object[]{
                internship.getId(),
                internship.getTitle(),
                supervisorName,
                internship.getStatus(),
                startDateStr,
                endDateStr
            });
        }
    }

    private void loadInternshipDetails(int selectedRowIndex) throws SQLException {
        if (selectedRowIndex < 0) return;
        int internshipId = (int) internshipsTableModel.getValueAt(selectedRowIndex, 0);
        Internship selectedInternship = internshipDAO.getInternshipById(internshipId);

        if (selectedInternship != null) {
            internshipTitleLabel.setText(selectedInternship.getTitle());
            descriptionArea.setText(selectedInternship.getDescription() != null ? selectedInternship.getDescription() : "");
            Supervisor supervisor = supervisorDAO.getSupervisorById(selectedInternship.getSupervisorId());
            supervisorLabel.setText(supervisor != null ? supervisor.getFirstName() + " " + supervisor.getLastName() + " (" + supervisor.getDepartment() + ")" : "N/A");
            statusLabel.setText(selectedInternship.getStatus());
            internshipStartDateLabel.setText(selectedInternship.getStartDate() != null ? dateFormat.format(selectedInternship.getStartDate()) : "N/A");
            internshipEndDateLabel.setText(selectedInternship.getEndDate() != null ? dateFormat.format(selectedInternship.getEndDate()) : "N/A");
            gradeLabel.setText(selectedInternship.getGrade() != null ? String.valueOf(selectedInternship.getGrade()) : "Non noté");
            feedbackArea.setText(selectedInternship.getFeedback() != null ? selectedInternship.getFeedback() : "Aucun feedback");
            reportPathTextField.setText(selectedInternship.getReportFilePath() != null ? selectedInternship.getReportFilePath() : "");
        } else {
            clearInternshipDetails();
        }
    }

    private void clearInternshipDetails() {
        internshipTitleLabel.setText("-");
        descriptionArea.setText("");
        supervisorLabel.setText("-");
        statusLabel.setText("-");
        internshipStartDateLabel.setText("-");
        internshipEndDateLabel.setText("-");
        gradeLabel.setText("-");
        feedbackArea.setText("");
        reportPathTextField.setText("");
    }

    private void browseForReport() {
        int selectedRow = internshipsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un stage.", "Aucun Stage Sélectionné", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner le fichier du rapport");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            reportPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void submitReport() throws SQLException {
        int selectedRow = internshipsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un stage.", "Aucun Stage Sélectionné", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int internshipId = (int) internshipsTableModel.getValueAt(selectedRow, 0);
        String reportPath = reportPathTextField.getText();
        if (reportPath == null || reportPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le chemin du rapport ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
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
        header.setBackground(secondaryColor);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Statut
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Début
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Fin
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Titre
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Superviseur
    }

    // Main method for testing (optional, usually in a separate Main class)
    public static void main(String[] args) {
        // This is for testing. In a real app, User object would come from LoginFrame.
        // Ensure you have a test user with role STAGIAIRE and corresponding stagiaire entry.
        User testUser = new User();
        testUser.setId(3); // <<<< IMPORTANT: Replace with an ACTUAL user_id of a STAGIAIRE in your DB
        testUser.setUsername("test.stagiaire"); // Replace with actual username
        testUser.setRole("STAGIAIRE");
        testUser.setEmail("test@example.com");

        SwingUtilities.invokeLater(() -> {
            // Check if user exists and is a stagiaire before launching
            UserDAO uDao = new UserDAO();
            User fetchedUser = uDao.getUserById(testUser.getId());
            if(fetchedUser != null && "STAGIAIRE".equalsIgnoreCase(fetchedUser.getRole())){
                StagiaireDashboard dashboard = null;
                try {
                    dashboard = new StagiaireDashboard(fetchedUser);
                } catch (SQLException ex) {
                    Logger.getLogger(StagiaireDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
                dashboard.setVisible(true);
            } else {
                System.err.println("Utilisateur de test non trouvé ou n'est pas un stagiaire. Vérifiez l'ID: " + testUser.getId());
                // Optionally show login frame
                 new LoginFrame().setVisible(true);
            }
        });
    }
}

