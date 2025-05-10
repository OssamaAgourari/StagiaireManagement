/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaire.ui.Admin;

/**
 *
 * @author agour
 */
import GestionStagiaire.models.*;
import GestionStagiaire.ui.LoginFrame;
import GestionStagiaireDao.InternshipDAO;
import GestionStagiaireDao.StagiaireDAO;
import GestionStagiaireDao.SupervisorDAO;
import GestionStagiaireDao.UserDAO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private Color primaryColor = new Color(0, 102, 204); // Dark blue
    private Color secondaryColor = new Color(240, 240, 240); // Light gray
    private Color accentColor = new Color(0, 153, 255); // Light blue
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("Admin Dashboard - Stagiaire Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set application icon
        ImageIcon img = new ImageIcon(getClass().getResource("/resources/icon.png"));
        if (img.getImage() != null) {
            setIconImage(img.getImage());
        }
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create menu bar
        createMenuBar();
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create tabbed pane for different functionalities
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Custom tabbed pane UI
        UIManager.put("TabbedPane.selected", primaryColor);
        UIManager.put("TabbedPane.borderHightlightColor", primaryColor);
        SwingUtilities.updateComponentTreeUI(tabbedPane);
        
        // Add tabs with icons
        // In your AdminDashboard constructor:

// 1. First fix the tab creation with correct panel methods
tabbedPane.addTab("Users", 
    new ImageIcon(new ImageIcon(getClass().getResource("/resources/users.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), 
    createUsersPanel(),  // Correct panel for Users
    "Manage System Users");

tabbedPane.addTab("Stagiaires", 
    new ImageIcon(new ImageIcon(getClass().getResource("/resources/interns.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), 
    createStagiairesPanel(),  // Changed to correct method
    "Manage Interns");

tabbedPane.addTab("Supervisors", 
    new ImageIcon(new ImageIcon(getClass().getResource("/resources/supervisors.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), 
    createSupervisorsPanel(),  // Changed to correct method
    "Manage Supervisors");

tabbedPane.addTab("Internships", 
    new ImageIcon(new ImageIcon(getClass().getResource("/resources/internships.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), 
    createInternshipsPanel(),  // Changed to correct method
    "Manage Internships");

// 2. Add this change listener to handle tab clicks properly
tabbedPane.addChangeListener(e -> {
    int selectedIndex = tabbedPane.getSelectedIndex();
    Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
    if (selectedComponent instanceof JScrollPane) {
        JScrollPane scrollPane = (JScrollPane) selectedComponent;
        scrollPane.getViewport().getView().repaint();
    }
});
        // Main content panel with proper spacing
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    
    
    private ImageIcon createIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            // Return empty icon if image not found
            return new ImageIcon();
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Stagiaire Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        
        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.BLACK);
        
        JLabel roleLabel = new JLabel("(" + currentUser.getRole() + ")");
        roleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        roleLabel.setForeground(new Color(200, 200, 200));
        
        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(roleLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        statusPanel.setBackground(secondaryColor);
        
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(primaryColor);
        menuBar.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // File menu
        JMenu fileMenu = createMenu("File", 'F');
        JMenuItem logoutItem = createMenuItem("Logout", 'L', KeyEvent.VK_L, 
            KeyEvent.CTRL_DOWN_MASK, createIcon("/resources/logout.png"));
        logoutItem.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        JMenuItem exitItem = createMenuItem("Exit", 'x', KeyEvent.VK_X, 
            KeyEvent.CTRL_DOWN_MASK, createIcon("/resources/exit.png"));
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = createMenu("Help", 'H');
        JMenuItem aboutItem = createMenuItem("About", 'A', KeyEvent.VK_A, 
            KeyEvent.CTRL_DOWN_MASK, createIcon("/resources/about.png"));
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenu createMenu(String text, char mnemonic) {
        JMenu menu = new JMenu(text);
        menu.setMnemonic(mnemonic);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menu.setForeground(Color.BLACK);
        return menu;
    }
    
    private JMenuItem createMenuItem(String text, char mnemonic, int keyCode, int modifiers, Icon icon) {
    JMenuItem item = new JMenuItem(text, mnemonic);
    item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
    item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    
    // Resize the icon if it exists
    if (icon != null) {
        ImageIcon originalIcon = (ImageIcon) icon;
        Image img = originalIcon.getImage();
        // Standard menu icon size (16x16)
        Image resizedImg = img.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        item.setIcon(new ImageIcon(resizedImg));
        item.setIconTextGap(5); // Add some space between icon and text
    }
    
    return item;
}
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setSize(400, 250);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Stagiaire Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JLabel versionLabel = new JLabel("Version 1.0.0", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel copyrightLabel = new JLabel("© 2023 Your Company", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setText("A professional system for managing interns, supervisors and internships.\n\n" +
                        "Developed using Java Swing and MySQL.");
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(versionLabel, BorderLayout.CENTER);
        contentPanel.add(infoArea, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(okButton);
        
        aboutDialog.add(contentPanel, BorderLayout.CENTER);
        aboutDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.setVisible(true);
    }
    
    // Users Management Panel with professional styling
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(secondaryColor);
        
        // Table model for users
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{"ID", "Username", "Role", "Email", "Created At"});
        
        // Create styled table
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom header renderer
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        
        // Center ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        // Load data
        loadUsersData(model);
        
        // Buttons panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(secondaryColor);
        
        JButton addButton = createStyledButton("Add User", primaryColor, Color.BLACK);
        addButton.addActionListener(e -> showUserForm(null, model));
        
        JButton editButton = createStyledButton("Edit User", accentColor, Color.BLACK);
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) table.getValueAt(selectedRow, 0);
                UserDAO dao = new UserDAO();
                User user = dao.getUserById(userId);
                showUserForm(user, model);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton deleteButton = createStyledButton("Delete User", new Color(204, 0, 0), Color.BLACK);
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this user?", "Confirm", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    UserDAO dao = new UserDAO();
                    if (dao.deleteUser(userId)) {
                        model.removeRow(selectedRow);
                        showMessage("User deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showMessage("Failed to delete user", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showMessage("Please select a user to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton refreshButton = createStyledButton("Refresh", new Color(100, 100, 100), Color.BLACK);
        refreshButton.addActionListener(e -> loadUsersData(model));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    private void showUserForm(User user, DefaultTableModel model) {
    JDialog dialog = new JDialog(this, user == null ? "Add User" : "Edit User", true);
    dialog.setSize(400, 300);
    
    JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
    
    // Form fields
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JComboBox<String> roleCombo = new JComboBox<>(new String[]{"ADMIN", "SUPERVISOR", "STAGIAIRE"});
    JTextField emailField = new JTextField();

    if (user != null) {
        usernameField.setText(user.getUsername());
        roleCombo.setSelectedItem(user.getRole());
        emailField.setText(user.getEmail());
    }

    panel.add(new JLabel("Username:"));
    panel.add(usernameField);
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);
    panel.add(new JLabel("Role:"));
    panel.add(roleCombo);
    panel.add(new JLabel("Email:"));
    panel.add(emailField);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    // THIS IS THE SAVE ACTION IMPLEMENTATION
    saveButton.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();
        String email = emailField.getText().trim();

        // Validation
        if (username.isEmpty() || email.isEmpty() || (user == null && password.isEmpty())) {
            JOptionPane.showMessageDialog(dialog, "Please fill all required fields", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO dao = new UserDAO();
        boolean success;
        
        if (user == null) {
            // Add new user
            User newUser = new User(0, username, password, role, email);
            success = dao.addUser(newUser);
        } else {
            // Update existing user
            user.setUsername(username);
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            user.setRole(role);
            user.setEmail(email);
            success = dao.updateUser(user);
        }

        if (success) {
            loadUsersData(model);  // Refresh the table
            dialog.dispose();
            JOptionPane.showMessageDialog(this, 
                "User saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to save user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    panel.add(saveButton);
    panel.add(cancelButton);

    dialog.add(panel);
    dialog.setVisible(true);
}
private void loadUsersData(DefaultTableModel model) {
    model.setRowCount(0); // Clear existing data
    UserDAO dao = new UserDAO();
    List<User> users = dao.getAllUsers();
    
    for (User user : users) {
        model.addRow(new Object[]{
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getEmail(),
            user.getCreatedAt()
        });
    }
}
    
    

    private JPanel createStagiairesPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    // Table model
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    model.setColumnIdentifiers(new String[]{"ID", "First Name", "Last Name", "CIN", "University", "Field", "Start Date", "End Date"});
    
    // Create table with custom renderer
    JTable table = new JTable(model);
    table.setRowHeight(25);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Center ID column
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
    
    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    
    JButton addButton = createStyledButton("Add Stagiaire", primaryColor, Color.BLACK);
    JButton editButton = createStyledButton("Edit", accentColor, Color.BLACK);
    JButton deleteButton = createStyledButton("Delete", new Color(204, 0, 0), Color.BLACK);
    JButton refreshButton = createStyledButton("Refresh", new Color(100, 100, 100), Color.BLACK);
    
    // Button actions
    addButton.addActionListener(e -> showStagiaireForm(null, model));
    editButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int stagiaireId = (int) table.getValueAt(selectedRow, 0);
            // Implement getStagiaireById in your DAO
            Stagiaire stagiaire = new StagiaireDAO().getStagiaireById(stagiaireId);
            showStagiaireForm(stagiaire, model);
        } else {
            showMessage("Please select a stagiaire to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    deleteButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this stagiaire?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int stagiaireId = (int) table.getValueAt(selectedRow, 0);
                if (new StagiaireDAO().deleteStagiaire(stagiaireId)) {
                    model.removeRow(selectedRow);
                    showMessage("Stagiaire deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showMessage("Failed to delete stagiaire", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            showMessage("Please select a stagiaire to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    refreshButton.addActionListener(e -> loadStagiairesData(model));
    
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);
    
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Load initial data
    loadStagiairesData(model);
    
    return panel;
}

private void loadStagiairesData(DefaultTableModel model) {
    model.setRowCount(0);
    List<Stagiaire> stagiaires = new StagiaireDAO().getAllStagiaires();
    for (Stagiaire stagiaire : stagiaires) {
        model.addRow(new Object[]{
            stagiaire.getId(),
            stagiaire.getFirstName(),
            stagiaire.getLastName(),
            stagiaire.getCin(),
            stagiaire.getUniversity(),
            stagiaire.getFieldOfStudy(),
            stagiaire.getStartDate(),
            stagiaire.getEndDate()
        });
    }
}
// Replace JDateChooser with this combo-based date picker
private JPanel createDatePicker() {
    JPanel panel = new JPanel(new GridLayout(1, 3, 5, 5));
    
    // Day
    JComboBox<Integer> dayCombo = new JComboBox<>();
    for (int i = 1; i <= 31; i++) {
        dayCombo.addItem(i);
    }
    
    // Month
    JComboBox<String> monthCombo = new JComboBox<>(new String[]{
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    });
    
    // Year
    JComboBox<Integer> yearCombo = new JComboBox<>();
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // Year (continued)
    for (int i = currentYear - 10; i <= currentYear + 10; i++) {
        yearCombo.addItem(i);
    }
    yearCombo.setSelectedItem(currentYear);
    
    panel.add(dayCombo);
    panel.add(monthCombo);
    panel.add(yearCombo);
    
    return panel;
}

// Method to get selected date
private Date getSelectedDate(JPanel datePickerPanel) {
    try {
        JComboBox<Integer> dayCombo = (JComboBox<Integer>) datePickerPanel.getComponent(0);
        JComboBox<String> monthCombo = (JComboBox<String>) datePickerPanel.getComponent(1);
        JComboBox<Integer> yearCombo = (JComboBox<Integer>) datePickerPanel.getComponent(2);
        
        int day = (int) dayCombo.getSelectedItem();
        int month = monthCombo.getSelectedIndex();
        int year = (int) yearCombo.getSelectedItem();
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    } catch (Exception e) {
        return null;
    }
}

    private void showStagiaireForm(Stagiaire stagiaire, DefaultTableModel model) {
    JDialog dialog = new JDialog(this, stagiaire == null ? "Add Stagiaire" : "Edit Stagiaire", true);
    dialog.setSize(500, 500);
    dialog.setLocationRelativeTo(this);
    
    // Date format for parsing and display
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Form fields
    JLabel firstNameLabel = new JLabel("First Name:");
    JTextField firstNameField = new JTextField();
    
    JLabel lastNameLabel = new JLabel("Last Name:");
    JTextField lastNameField = new JTextField();
    
    JLabel cinLabel = new JLabel("CIN:");
    JTextField cinField = new JTextField();
    
    JLabel dobLabel = new JLabel("Date of Birth (yyyy-MM-dd):");
    JTextField dobField = new JTextField();
    
    JLabel universityLabel = new JLabel("University:");
    JTextField universityField = new JTextField();
    
    JLabel fieldLabel = new JLabel("Field of Study:");
    JTextField fieldField = new JTextField();
    
    JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
    JTextField startDateField = new JTextField();
    
    JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
    JTextField endDateField = new JTextField();
    
    // Populate fields if editing
    if (stagiaire != null) {
        firstNameField.setText(stagiaire.getFirstName());
        lastNameField.setText(stagiaire.getLastName());
        cinField.setText(stagiaire.getCin());
        dobField.setText(stagiaire.getDateOfBirth() != null ? 
                        dateFormat.format(stagiaire.getDateOfBirth()) : "");
        universityField.setText(stagiaire.getUniversity());
        fieldField.setText(stagiaire.getFieldOfStudy());
        startDateField.setText(stagiaire.getStartDate() != null ? 
                             dateFormat.format(stagiaire.getStartDate()) : "");
        endDateField.setText(stagiaire.getEndDate() != null ? 
                           dateFormat.format(stagiaire.getEndDate()) : "");
    }
    
    // Add fields to form
    formPanel.add(firstNameLabel);
    formPanel.add(firstNameField);
    formPanel.add(lastNameLabel);
    formPanel.add(lastNameField);
    formPanel.add(cinLabel);
    formPanel.add(cinField);
    formPanel.add(dobLabel);
    formPanel.add(dobField);
    formPanel.add(universityLabel);
    formPanel.add(universityField);
    formPanel.add(fieldLabel);
    formPanel.add(fieldField);
    formPanel.add(startDateLabel);
    formPanel.add(startDateField);
    formPanel.add(endDateLabel);
    formPanel.add(endDateField);
    
    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        try {
            
            Date dob = dobField.getText().isEmpty() ? null : dateFormat.parse(dobField.getText());
            Date startDate = startDateField.getText().isEmpty() ? null : dateFormat.parse(startDateField.getText());
            Date endDate = endDateField.getText().isEmpty() ? null : dateFormat.parse(endDateField.getText());
            
            // Validate dates
            if (startDate != null && endDate != null && startDate.after(endDate)) {
                JOptionPane.showMessageDialog(dialog, 
                    "End date must be after start date", 
                    "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            StagiaireDAO dao = new StagiaireDAO();
            boolean success;
            if (stagiaire == null) {
                // Create new stagiaire
                Stagiaire newStagiaire = new Stagiaire(
                    0,
                    getNextUserId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    cinField.getText(),
                    dob,
                    universityField.getText(),
                    fieldField.getText(),
                    startDate,
                    endDate
                );
                success = dao.addStagiaire(newStagiaire);
            } else {
                // Update existing stagiaire
                stagiaire.setFirstName(firstNameField.getText());
                stagiaire.setLastName(lastNameField.getText());
                stagiaire.setCin(cinField.getText());
                stagiaire.setDateOfBirth(dob);
                stagiaire.setUniversity(universityField.getText());
                stagiaire.setFieldOfStudy(fieldField.getText());
                stagiaire.setStartDate(startDate);
                stagiaire.setEndDate(endDate);
                success = dao.updateStagiaire(stagiaire);
            }
            
            if (success) {
                loadStagiairesData(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, 
                    "Stagiaire saved successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to save stagiaire", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter dates in yyyy-MM-dd format", 
                "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    
    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
}

private int getNextUserId() {
    // Fetch the next available user ID. This could be from the database or from a custom counter.
    StagiaireDAO dao = new StagiaireDAO();
    int nextId = dao.getNextUserId();  // This is a method you'd implement in StagiaireDAO.
    return nextId;
}


private void showSupervisorForm(Supervisor supervisor, DefaultTableModel model) {
    JDialog dialog = new JDialog(this, supervisor == null ? "Add Supervisor" : "Edit Supervisor", true);
    dialog.setSize(450, 350);
    dialog.setLocationRelativeTo(this);
    
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Form fields
    JLabel firstNameLabel = new JLabel("First Name:");
    JTextField firstNameField = new JTextField();
    
    JLabel lastNameLabel = new JLabel("Last Name:");
    JTextField lastNameField = new JTextField();
    
    JLabel departmentLabel = new JLabel("Department:");
    JTextField departmentField = new JTextField();
    
    JLabel positionLabel = new JLabel("Position:");
    JTextField positionField = new JTextField();
    
    JLabel emailLabel = new JLabel("Email:");
    JTextField emailField = new JTextField();
    
    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    
    // Populate if editing
    if (supervisor != null) {
        User user = new UserDAO().getUserById(supervisor.getUserId());
        firstNameField.setText(supervisor.getFirstName());
        lastNameField.setText(supervisor.getLastName());
        departmentField.setText(supervisor.getDepartment());
        positionField.setText(supervisor.getPosition());
        emailField.setText(user.getEmail());
        usernameField.setText(user.getUsername());
    }
    
    // Add fields to form
    formPanel.add(firstNameLabel);
    formPanel.add(firstNameField);
    formPanel.add(lastNameLabel);
    formPanel.add(lastNameField);
    formPanel.add(departmentLabel);
    formPanel.add(departmentField);
    formPanel.add(positionLabel);
    formPanel.add(positionField);
    formPanel.add(emailLabel);
    formPanel.add(emailField);
    formPanel.add(usernameLabel);
    formPanel.add(usernameField);
    formPanel.add(passwordLabel);
    formPanel.add(passwordField);
    
    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton saveButton = createStyledButton("Save", primaryColor, Color.BLACK);
    JButton cancelButton = createStyledButton("Cancel", new Color(100, 100, 100), Color.BLACK);
    
    saveButton.addActionListener(e -> {
        // Validation and save logic
        SupervisorDAO supervisorDAO = new SupervisorDAO();
        UserDAO userDAO = new UserDAO();
        boolean success = false;
        
        if (supervisor == null) {
            // Create new supervisor
            User user = new User(
                0,
                usernameField.getText(),
                new String(passwordField.getPassword()),
                "SUPERVISOR",
                emailField.getText()
            );
            
            if (userDAO.addUser(user)) {
                Supervisor newSupervisor = new Supervisor(
                    0,
                    user.getId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    departmentField.getText(),
                    positionField.getText()
                );
                success = supervisorDAO.addSupervisor(newSupervisor);
            }
        } else {
            // Update existing supervisor
            User user = userDAO.getUserById(supervisor.getUserId());
            user.setEmail(emailField.getText());
            user.setUsername(usernameField.getText());
            if (passwordField.getPassword().length > 0) {
                user.setPassword(new String(passwordField.getPassword()));
            }
            
            if (userDAO.updateUser(user)) {
                supervisor.setFirstName(firstNameField.getText());
                supervisor.setLastName(lastNameField.getText());
                supervisor.setDepartment(departmentField.getText());
                supervisor.setPosition(positionField.getText());
                success = supervisorDAO.updateSupervisor(supervisor);
            }
        }
        
        if (success) {
            loadSupervisorsData(model);
            dialog.dispose();
            showMessage("Supervisor saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Failed to save supervisor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    
    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
}
private JPanel createSupervisorsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    // Table model
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    model.setColumnIdentifiers(new String[]{"ID", "First Name", "Last Name", "Department", "Position", "Email"});
    
    // Create table with custom styling
    JTable table = new JTable(model);
    table.setRowHeight(25);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Center ID column
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
    
    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    
    JButton addButton = new JButton("Add Supervisor");
    JButton editButton = new JButton("Edit");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");
    
    // Button actions
    addButton.addActionListener(e -> showSupervisorForm(null, model));
    editButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int supervisorId = (int) table.getValueAt(selectedRow, 0);
            Supervisor supervisor = new SupervisorDAO().getSupervisorById(supervisorId);
            showSupervisorForm(supervisor, model);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select a supervisor to edit", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    deleteButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this supervisor?", 
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int supervisorId = (int) table.getValueAt(selectedRow, 0);
                if (new SupervisorDAO().deleteSupervisor(supervisorId)) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, 
                        "Supervisor deleted successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete supervisor", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select a supervisor to delete", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    refreshButton.addActionListener(e -> loadSupervisorsData(model));
    
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);
    
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Load initial data
    loadSupervisorsData(model);
    
    return panel;
}

private void loadSupervisorsData(DefaultTableModel model) {
    model.setRowCount(0); // Clear existing data
    SupervisorDAO supervisorDAO = new SupervisorDAO();
    UserDAO userDAO = new UserDAO();
    
    List<Supervisor> supervisors = supervisorDAO.getAllSupervisors();
    for (Supervisor supervisor : supervisors) {
        User user = userDAO.getUserById(supervisor.getUserId());
        model.addRow(new Object[]{
            supervisor.getId(),
            supervisor.getFirstName(),
            supervisor.getLastName(),
            supervisor.getDepartment(),
            supervisor.getPosition(),
            user != null ? user.getEmail() : "N/A"
        });
    }
}



    
private void refreshInternshipsData(DefaultTableModel model) {
    model.setRowCount(0);
}




private JPanel createInternshipsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Table model
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make table non-editable
        }
    };
    model.setColumnIdentifiers(new String[]{"ID", "Title", "Stagiaire", "Supervisor", "Status", "Start Date", "End Date"});

    // Create table with custom styling
    JTable table = new JTable(model);
    table.setRowHeight(25);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Center ID column
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));

    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    
    JButton addButton = new JButton("Add Internship");
    JButton editButton = new JButton("Edit");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");

    // Button actions
    addButton.addActionListener(e -> showInternshipForm(null, model));
    
    editButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int internshipId = (int) table.getValueAt(selectedRow, 0);
            try {
                Internship internship = new InternshipDAO().getInternshipById(internshipId);
                if (internship != null) {
                    showInternshipForm(internship, model);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Internship not found", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select an internship to edit", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    deleteButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this internship?", 
                "Confirm", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                int internshipId = (int) table.getValueAt(selectedRow, 0);
                try {
                    if (new InternshipDAO().deleteInternship(internshipId)) {
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, 
                            "Internship deleted successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to delete internship", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Database error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select an internship to delete", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    refreshButton.addActionListener(e -> loadInternshipsData(model));

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    // Load initial data
    loadInternshipsData(model);

    return panel;
}

private void loadInternshipsData(DefaultTableModel model) {
    model.setRowCount(0); // Clear existing data
    try {
        List<Internship> internships = new InternshipDAO().getAllInternships();
        StagiaireDAO stagiaireDao = new StagiaireDAO();
        SupervisorDAO supervisorDao = new SupervisorDAO();
        
        for (Internship internship : internships) {
            Stagiaire stagiaire = stagiaireDao.getStagiaireById(internship.getStagiaireId());
            Supervisor supervisor = supervisorDao.getSupervisorById(internship.getSupervisorId());
            
            model.addRow(new Object[]{
                internship.getId(),
                internship.getTitle(),
                stagiaire != null ? stagiaire.getFirstName() + " " + stagiaire.getLastName() : "N/A",
                supervisor != null ? supervisor.getFirstName() + " " + supervisor.getLastName() : "N/A",
                internship.getStatus(),
                internship.getStartDate(),
                internship.getEndDate()
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error loading internships: " + ex.getMessage(), 
            "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private void showInternshipForm(Internship internship, DefaultTableModel model) {
    JDialog dialog = new JDialog(this, 
        internship == null ? "Add Internship" : "Edit Internship", 
        true);
    dialog.setSize(600, 500);
    dialog.setLocationRelativeTo(this);
    
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

    // Form fields
    JLabel titleLabel = new JLabel("Title*:");
    JTextField titleField = new JTextField();
    
    JLabel stagiaireLabel = new JLabel("Stagiaire*:");
    JComboBox<String> stagiaireCombo = new JComboBox<>();
    
    JLabel supervisorLabel = new JLabel("Supervisor*:");
    JComboBox<String> supervisorCombo = new JComboBox<>();
    
    JLabel statusLabel = new JLabel("Status*:");
    JComboBox<String> statusCombo = new JComboBox<>(
        new String[]{"PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"});
    
    JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd)*:");
    JTextField startDateField = new JTextField();
    
    JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd)*:");
    JTextField endDateField = new JTextField();
    
    JLabel descriptionLabel = new JLabel("Description:");
    JTextArea descriptionArea = new JTextArea(3, 20);
    JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

    // Load combobox data
    try {
        loadStagiairesCombo(stagiaireCombo);
        loadSupervisorsCombo(supervisorCombo);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(dialog,
            "Error loading data: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    // Populate if editing
    if (internship != null) {
        titleField.setText(internship.getTitle());
        statusCombo.setSelectedItem(internship.getStatus());
        startDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(internship.getStartDate()));
        endDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(internship.getEndDate()));
        descriptionArea.setText(internship.getDescription());
        
        // Select current stagiaire/supervisor
        selectComboValue(stagiaireCombo, internship.getStagiaireId());
        selectComboValue(supervisorCombo, internship.getSupervisorId());
    }

    // Add fields to form
    formPanel.add(titleLabel);
    formPanel.add(titleField);
    formPanel.add(stagiaireLabel);
    formPanel.add(stagiaireCombo);
    formPanel.add(supervisorLabel);
    formPanel.add(supervisorCombo);
    formPanel.add(statusLabel);
    formPanel.add(statusCombo);
    formPanel.add(startDateLabel);
    formPanel.add(startDateField);
    formPanel.add(endDateLabel);
    formPanel.add(endDateField);
    formPanel.add(descriptionLabel);
    formPanel.add(descriptionScroll);

    // Save button
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
        try {
            // Validate required fields
            if (titleField.getText().trim().isEmpty() ||
                stagiaireCombo.getSelectedItem() == null ||
                supervisorCombo.getSelectedItem() == null ||
                startDateField.getText().isEmpty() ||
                endDateField.getText().isEmpty()) {
                
                JOptionPane.showMessageDialog(dialog,
                    "Please fill all required fields (*)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateField.getText());
            Date endDate = dateFormat.parse(endDateField.getText());
            
            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(dialog,
                    "End date must be after start date",
                    "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get selected IDs from combos
            int stagiaireId = getSelectedIdFromCombo(stagiaireCombo);
            int supervisorId = getSelectedIdFromCombo(supervisorCombo);

            // Prepare internship object
            Internship internshipToSave;
            if (internship == null) {
                internshipToSave = new Internship(
                    0, // new record
                    stagiaireId,
                    supervisorId,
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    (String) statusCombo.getSelectedItem(),
                    startDate,
                    endDate
                );
            } else {
                internship.setStagiaireId(stagiaireId);
                internship.setSupervisorId(supervisorId);
                internship.setTitle(titleField.getText().trim());
                internship.setDescription(descriptionArea.getText().trim());
                internship.setStatus((String) statusCombo.getSelectedItem());
                internship.setStartDate(startDate);
                internship.setEndDate(endDate);
                internshipToSave = internship;
            }

            // Save to database
            boolean success;
            if (internship == null) {
                success = new InternshipDAO().addInternship(internshipToSave);
            } else {
                success = new InternshipDAO().updateInternship(internshipToSave);
            }

            if (success) {
                loadInternshipsData(model); // Refresh table
                dialog.dispose(); // Close form
                JOptionPane.showMessageDialog(this,
                    "Internship saved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new Exception("Database operation failed");
            }
            
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(dialog,
                "Invalid date format. Please use yyyy-MM-dd",
                "Date Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                "Error saving internship: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dialog.dispose());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
}

// Helper methods for combo boxes
private void loadStagiairesCombo(JComboBox<String> combo) throws SQLException {
    combo.removeAllItems();
    List<Stagiaire> stagiaires = new StagiaireDAO().getAllStagiaires();
    for (Stagiaire s : stagiaires) {
        combo.addItem(s.getId() + " - " + s.getFirstName() + " " + s.getLastName());
    }
}

private void loadSupervisorsCombo(JComboBox<String> combo) throws SQLException {
    combo.removeAllItems();
    List<Supervisor> supervisors = new SupervisorDAO().getAllSupervisors();
    for (Supervisor s : supervisors) {
        combo.addItem(s.getId() + " - " + s.getFirstName() + " " + s.getLastName());
    }
}

private void selectComboValue(JComboBox<String> combo, int id) {
    for (int i = 0; i < combo.getItemCount(); i++) {
        if (combo.getItemAt(i).startsWith(id + " - ")) {
            combo.setSelectedIndex(i);
            break;
        }
    }
}

private int getSelectedIdFromCombo(JComboBox<String> combo) {
    String selected = (String) combo.getSelectedItem();
    return Integer.parseInt(selected.split(" - ")[0]);
}
    
}