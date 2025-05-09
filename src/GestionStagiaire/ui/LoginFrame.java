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

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("Stagiaire Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Please enter both username and password", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                UserDAO userDAO = new UserDAO();
                User user = userDAO.authenticate(username, password);
                
                if (user != null) {
                    openRoleSpecificWindow(user);
                    dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Invalid username or password", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton, gbc);
        
        add(panel);
    }
    
    private void openRoleSpecificWindow(User user) {
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
