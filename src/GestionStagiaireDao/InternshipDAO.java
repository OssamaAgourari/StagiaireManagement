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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public Internship getInternshipByStagiaireId(int stagiaireId) {
    String sql = "SELECT i.*, u.username as supervisor_username " +
                 "FROM internships i " +
                 "JOIN supervisors s ON i.supervisor_id = s.user_id " +
                 "JOIN users u ON s.user_id = u.id " +
                 "WHERE i.stagiaire_id = ?";
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, stagiaireId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Internship internship = new Internship();
            internship.setId(rs.getInt("id"));
            internship.setStagiaireId(rs.getInt("stagiaire_id"));
            internship.setSupervisorId(rs.getInt("supervisor_id"));
            internship.setTitle(rs.getString("title"));
            internship.setDescription(rs.getString("description"));
            internship.setStatus(rs.getString("status"));
            internship.setStartDate(rs.getDate("start_date"));
            internship.setEndDate(rs.getDate("end_date"));
            
            return internship;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Consider using a proper logging mechanism
    }
    return null;
}
    public String getSupervisorName(int supervisorId) {
    String sql = "SELECT first_name, last_name FROM supervisors WHERE id = ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, supervisorId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("first_name") + " " + rs.getString("last_name");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return "Unknown";
}
    
    public boolean uploadReport(int internshipId, byte[] fileContent) {
    String filePath = "reports/internship_" + internshipId + ".pdf";
    
    // Save the file to the file system
    try {
        Files.createDirectories(Paths.get("reports"));
        Files.write(Paths.get(filePath), fileContent);
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    // Save the file path to the database
    String sql = "UPDATE internships SET report_file_path = ? WHERE id = ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, filePath);
        stmt.setInt(2, internshipId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}
    
    public byte[] downloadReport(int internshipId) {
    String sql = "SELECT report_file_path FROM internships WHERE id = ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, internshipId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String path = rs.getString("report_file_path");
            if (path != null && !path.isEmpty()) {
                return Files.readAllBytes(Paths.get(path));
            }
        }
    } catch (SQLException | IOException ex) {
        ex.printStackTrace();
    }
    return null;
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
    
        // Get internships for a specific supervisor
    public List<Internship> getInternshipsBySupervisorId(int supervisorId) {
        List<Internship> internships = new ArrayList<>();
        String sql = "SELECT * FROM internships WHERE supervisor_id = ? ORDER BY start_date DESC, id DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, supervisorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Internship internship = new Internship();
                    internship.setId(rs.getInt("id"));
                    internship.setStagiaireId(rs.getInt("stagiaire_id"));
                    internship.setSupervisorId(rs.getInt("supervisor_id"));
                    internship.setTitle(rs.getString("title"));
                    internship.setDescription(rs.getString("description"));
                    internship.setStatus(rs.getString("status"));
                    internship.setReportFilePath(rs.getString("report_file_path"));
                    
                    // Handle potential null for grade
                    double gradeValue = rs.getDouble("grade");
                    if (rs.wasNull()) {
                        internship.setGrade(null);
                    } else {
                        internship.setGrade(gradeValue);
                    }
                    
                    internship.setFeedback(rs.getString("feedback"));
                    
                    // Handle dates (start_date and end_date from internships table)
                    java.sql.Date startDateSql = rs.getDate("start_date");
                    if (startDateSql != null) {
                        internship.setStartDate(new java.util.Date(startDateSql.getTime()));
                    }
                    java.sql.Date endDateSql = rs.getDate("end_date");
                    if (endDateSql != null) {
                        internship.setEndDate(new java.util.Date(endDateSql.getTime()));
                    }
                    
                    internships.add(internship);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des stages par supervisor_id: " + e.getMessage());
            // Consider logging the error or throwing a custom exception
            e.printStackTrace(); 
        }
        return internships;
    }

    // Method to update internship status, grade, and feedback by supervisor
    // (This was suggested when providing SupervisorDashboard.java)
    public boolean updateInternshipEvaluation(int internshipId, String status, Double grade, String feedback) {
        String sql = "UPDATE internships SET status = ?, grade = ?, feedback = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            if (grade != null) {
                pstmt.setDouble(2, grade);
            } else {
                pstmt.setNull(2, java.sql.Types.DECIMAL);
            }
            pstmt.setString(3, feedback);
            pstmt.setInt(4, internshipId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l\'évaluation du stage: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // It seems you already have a getInternshipsByStagiaireId method, but it was named getInternshipsByStagiaire.
    // I'll rename it for consistency if you prefer, or you can keep both.
    // For clarity, here is the version from your file, slightly adapted to match the new one's style for date handling.
    public List<Internship> getInternshipsByStagiaireId(int stagiaireId) {
        List<Internship> internships = new ArrayList<>();
        // Original query was: "SELECT * FROM internships WHERE stagiaire_id = ? ORDER BY start_date";
        // Adding id DESC for consistent ordering if start_dates are the same.
        String sql = "SELECT * FROM internships WHERE stagiaire_id = ? ORDER BY start_date DESC, id DESC"; 
        
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
                    internship.setReportFilePath(rs.getString("report_file_path"));
                    double gradeValue = rs.getDouble("grade");
                    if (rs.wasNull()) {
                        internship.setGrade(null);
                    } else {
                        internship.setGrade(gradeValue);
                    }
                    internship.setFeedback(rs.getString("feedback"));
                    java.sql.Date startDateSql = rs.getDate("start_date");
                    if (startDateSql != null) {
                        internship.setStartDate(new java.util.Date(startDateSql.getTime()));
                    }
                    java.sql.Date endDateSql = rs.getDate("end_date");
                    if (endDateSql != null) {
                        internship.setEndDate(new java.util.Date(endDateSql.getTime()));
                    }
                    internships.add(internship);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des stages par stagiaire_id: " + e.getMessage());
            e.printStackTrace();
        }
        return internships;
    }

    // Method to update report path, useful for StagiaireDashboard
    public boolean updateInternshipReportPath(int internshipId, String reportFilePath) {
        String sql = "UPDATE internships SET report_file_path = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reportFilePath);
            pstmt.setInt(2, internshipId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du chemin du rapport de stage: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}