/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaire.ui;

/**
 *
 * @author agour
 */
import GestionStagiaire.models.User;
import GestionStagiaire.ui.Admin.AdminDashboard;
import GestionStagiaire.ui.Stagiaire.StagiaireDashboard;
import GestionStagiaire.ui.Supervisor.SupervisorDashboard;
import GestionStagiaireDao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
    // Window Configuration
    setTitle("Stagiaire Management System - Login");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    
    // Main Panel Setup
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);  // Increased padding
    gbc.anchor = GridBagConstraints.LINE_START;

    // Username Field
    gbc.gridx = 0;
    gbc.gridy = 0;
    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.BOLD));
    panel.add(usernameLabel, gbc);
    
    gbc.gridx = 1;
    usernameField = new JTextField(15);
    usernameField.setMargin(new Insets(5, 5, 5, 5));  // Text field padding
    panel.add(usernameField, gbc);
    
    // Password Field
    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(passwordLabel.getFont().deriveFont(Font.BOLD));
    panel.add(passwordLabel, gbc);
    
    gbc.gridx = 1;
    passwordField = new JPasswordField(15);
    passwordField.setMargin(new Insets(5, 5, 5, 5));
    panel.add(passwordField, gbc);
    
    // Login Button
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.CENTER;
    gbc.insets = new Insets(15, 0, 0, 0);  // Extra top margin
    
    JButton loginButton = new JButton("Login");
    loginButton.setPreferredSize(new Dimension(120, 30));
    loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD));
    loginButton.setBackground(new Color(0, 120, 215));  // Modern blue
    loginButton.setForeground(Color.WHITE);
    loginButton.setFocusPainted(false);
    loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    // Enhanced ActionListener with input cleanup
    loginButton.addActionListener(e -> {
        char[] passwordChars = passwordField.getPassword();
        String username = usernameField.getText().trim();
        String password = new String(passwordChars);
        
        // Clear password from memory ASAP
        Arrays.fill(passwordChars, '\0');
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(LoginFrame.this, 
                "Please enter both username and password", 
                "Authentication Required", 
                JOptionPane.WARNING_MESSAGE);  // Changed to warning
            return;
        }
        
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {
                try {
                    openRoleSpecificWindow(user);
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid credentials. Please try again.", 
                    "Authentication Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            // Additional cleanup
            passwordField.setText("");
        }
    });
    
    // Set Enter key to trigger login
    getRootPane().setDefaultButton(loginButton);
    
    panel.add(loginButton, gbc);
    add(panel);
    
    // Visual polish - set focus to username field on show
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowOpened(WindowEvent e) {
            usernameField.requestFocusInWindow();
        }
    });
}
    
    private void openRoleSpecificWindow(User user) throws SQLException {
        switch (user.getRole()) {
            case "ADMIN":
                new AdminDashboard(user).setVisible(true);
                break;
            case "SUPERVISOR":
                new SupervisorDashboard(user).setVisible(true);
                break;
            case "STAGIAIRE":
                new StagiaireDashboard(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, 
                        "Unknown user role", 
                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
