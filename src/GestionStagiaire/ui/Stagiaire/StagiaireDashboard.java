/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaire.ui.Stagiaire;

/**
 *
 * @author agour
 */
import GestionStagiaire.models.User;
import javax.swing.*;
import java.awt.*;

public class StagiaireDashboard extends JFrame {
    public StagiaireDashboard(User user) {
        setTitle("Stagiaire Dashboard - Welcome " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Profile menu
        JMenu profileMenu = new JMenu("Profile");
        JMenuItem viewProfileItem = new JMenuItem("View Profile");
        profileMenu.add(viewProfileItem);
        
        // Tasks menu
        JMenu tasksMenu = new JMenu("Tasks");
        JMenuItem viewTasksItem = new JMenuItem("View Tasks");
        tasksMenu.add(viewTasksItem);
        
        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem submitReportItem = new JMenuItem("Submit Report");
        reportsMenu.add(submitReportItem);
        
        // Add menus to menu bar
        menuBar.add(profileMenu);
        menuBar.add(tasksMenu);
        menuBar.add(reportsMenu);
        
        setJMenuBar(menuBar);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, Stagiaire " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
}