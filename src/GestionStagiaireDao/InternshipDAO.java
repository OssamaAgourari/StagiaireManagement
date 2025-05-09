/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaireDao;

/**
 *
 * @author agour
 */

import GestionStagiaire.models.Internship;
import gestionstagiares.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipDAO {
    
    // Create a new internship
    public boolean addInternship(Internship internship) throws SQLException {
        String sql = "INSERT INTO internships (stagiaire_id, supervisor_id, title, description, " +
                     "status, report_file_path, grade, feedback, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, internship.getStagiaireId());
            stmt.setInt(2, internship.getSupervisorId());
            stmt.setString(3, internship.getTitle());
            stmt.setString(4, internship.getDescription());
            stmt.setString(5, internship.getStatus());
            stmt.setString(6, internship.getReportFilePath());
            stmt.setObject(7, internship.getGrade(), Types.DOUBLE);
            stmt.setString(8, internship.getFeedback());
            stmt.setDate(9, new java.sql.Date(internship.getStartDate().getTime()));
            stmt.setDate(10, new java.sql.Date(internship.getEndDate().getTime()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        internship.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    // Get internship by ID
    public Internship getInternshipById(int id) throws SQLException {
        String sql = "SELECT * FROM internships WHERE id = ?";
        Internship internship = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    internship = new Internship();
                    internship.setId(rs.getInt("id"));
                    internship.setStagiaireId(rs.getInt("stagiaire_id"));
                    internship.setSupervisorId(rs.getInt("supervisor_id"));
                    internship.setTitle(rs.getString("title"));
                    internship.setDescription(rs.getString("description"));
                    internship.setStatus(rs.getString("status"));
                    internship.setStartDate(rs.getDate("start_date"));
                    internship.setEndDate(rs.getDate("end_date"));
                }
            }
        }
        return internship;
    }
    
    // Get all internships
    public List<Internship> getAllInternships() throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT * FROM internships ORDER BY start_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Internship internship = new Internship();
                internship.setId(rs.getInt("id"));
                internship.setStagiaireId(rs.getInt("stagiaire_id"));
                internship.setSupervisorId(rs.getInt("supervisor_id"));
                internship.setTitle(rs.getString("title"));
                internship.setDescription(rs.getString("description"));
                internship.setStatus(rs.getString("status"));
                internship.setReportFilePath(rs.getString("report_file_path"));
                internship.setGrade(rs.getDouble("grade"));
                if (rs.wasNull()) internship.setGrade(null);
                internship.setFeedback(rs.getString("feedback"));
                internship.setStartDate(rs.getDate("start_date"));
                internship.setEndDate(rs.getDate("end_date"));
                
                internships.add(internship);
            }
        }
        return internships;
    }
    
    // Update an existing internship
    public boolean updateInternship(Internship internship) throws SQLException {
        String sql = "UPDATE internships SET stagiaire_id = ?, supervisor_id = ?, title = ?, " +
                     "description = ?, status = ?, report_file_path = ?, grade = ?, feedback = ?, " +
                     "start_date = ?, end_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, internship.getStagiaireId());
            stmt.setInt(2, internship.getSupervisorId());
            stmt.setString(3, internship.getTitle());
            stmt.setString(4, internship.getDescription());
            stmt.setString(5, internship.getStatus());
            stmt.setString(6, internship.getReportFilePath());
            stmt.setObject(7, internship.getGrade(), Types.DOUBLE);
            stmt.setString(8, internship.getFeedback());
            stmt.setDate(9, new java.sql.Date(internship.getStartDate().getTime()));
            stmt.setDate(10, new java.sql.Date(internship.getEndDate().getTime()));
            stmt.setInt(11, internship.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    
    // Delete an internship
    public boolean deleteInternship(int id) throws SQLException {
        String sql = "DELETE FROM internships WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Additional useful methods
    
    // Get internships by status
    public List<Internship> getInternshipsByStatus(String status) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT * FROM internships WHERE status = ? ORDER BY start_date";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Internship internship = new Internship();
                    internship.setId(rs.getInt("id"));
                    internship.setStagiaireId(rs.getInt("stagiaire_id"));
                    internship.setSupervisorId(rs.getInt("supervisor_id"));
                    internship.setTitle(rs.getString("title"));
                    internship.setDescription(rs.getString("description"));
                    internship.setStatus(rs.getString("status"));
                    internship.setStartDate(rs.getDate("start_date"));
                    internship.setEndDate(rs.getDate("end_date"));
                    
                    internships.add(internship);
                }
            }
        }
        return internships;
    }
    
    // Get internships for a specific stagiaire
    public List<Internship> getInternshipsByStagiaire(int stagiaireId) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT * FROM internships WHERE stagiaire_id = ? ORDER BY start_date";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, stagiaireId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Internship internship = new Internship();
                    internship.setId(rs.getInt("id"));
                    internship.setStagiaireId(rs.getInt("stagiaire_id"));
                    internship.setSupervisorId(rs.getInt("supervisor_id"));
                    internship.setTitle(rs.getString("title"));
                    internship.setDescription(rs.getString("description"));
                    internship.setStatus(rs.getString("status"));
                    internship.setStartDate(rs.getDate("start_date"));
                    internship.setEndDate(rs.getDate("end_date"));
                    
                    internships.add(internship);
                }
            }
        }
        return internships;
    }
}