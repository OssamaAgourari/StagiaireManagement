/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaire.ui.Supervisor;

/**
 *
 * @author agour
 */
import GestionStagiaire.models.User;
import javax.swing.*;
import java.awt.*;

public class SupervisorDashboard extends JFrame {
    public SupervisorDashboard(User user) {
        setTitle("Supervisor Dashboard - Welcome " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Stagiaires menu
        JMenu stagiairesMenu = new JMenu("My Stagiaires");
        JMenuItem viewStagiairesItem = new JMenuItem("View Stagiaires");
        stagiairesMenu.add(viewStagiairesItem);
        
        // Tasks menu
        JMenu tasksMenu = new JMenu("Tasks");
        JMenuItem assignTasksItem = new JMenuItem("Assign Tasks");
        tasksMenu.add(assignTasksItem);
        
        // Evaluation menu
        JMenu evaluationMenu = new JMenu("Evaluation");
        JMenuItem evaluateStagiaireItem = new JMenuItem("Evaluate Stagiaire");
        evaluationMenu.add(evaluateStagiaireItem);
        
        // Add menus to menu bar
        menuBar.add(stagiairesMenu);
        menuBar.add(tasksMenu);
        menuBar.add(evaluationMenu);
        
        setJMenuBar(menuBar);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, Supervisor " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
}