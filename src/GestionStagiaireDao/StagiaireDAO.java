/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaireDao;
import GestionStagiaire.models.Stagiaire;
import gestionstagiares.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agour
 */
public class StagiaireDAO {
    public boolean addStagiaire(Stagiaire stagiaire) {
        String sql = "INSERT INTO stagiaires (user_id, first_name, last_name, cin, date_of_birth, " +
                     "university, field_of_study, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, stagiaire.getUserId());
            stmt.setString(2, stagiaire.getFirstName());
            stmt.setString(3, stagiaire.getLastName());
            stmt.setString(4, stagiaire.getCin());
            stmt.setDate(5, new java.sql.Date(stagiaire.getDateOfBirth().getTime()));
            stmt.setString(6, stagiaire.getUniversity());
            stmt.setString(7, stagiaire.getFieldOfStudy());
            stmt.setDate(8, new java.sql.Date(stagiaire.getStartDate().getTime()));
            stmt.setDate(9, new java.sql.Date(stagiaire.getEndDate().getTime()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        stagiaire.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read operations
    public Stagiaire getStagiaireById(int id) {
        String sql = "SELECT * FROM stagiaires WHERE id = ?";
        Stagiaire stagiaire = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stagiaire = new Stagiaire();
                    stagiaire.setId(rs.getInt("id"));
                    stagiaire.setUserId(rs.getInt("user_id"));
                    stagiaire.setFirstName(rs.getString("first_name"));
                    stagiaire.setLastName(rs.getString("last_name"));
                    stagiaire.setCin(rs.getString("cin"));
                    stagiaire.setDateOfBirth(rs.getDate("date_of_birth"));
                    stagiaire.setUniversity(rs.getString("university"));
                    stagiaire.setFieldOfStudy(rs.getString("field_of_study"));
                    stagiaire.setStartDate(rs.getDate("start_date"));
                    stagiaire.setEndDate(rs.getDate("end_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stagiaire;
    }

    public List<Stagiaire> getAllStagiaires() {
        List<Stagiaire> stagiaires = new ArrayList<>();
        String sql = "SELECT * FROM stagiaires ORDER BY last_name, first_name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Stagiaire stagiaire = new Stagiaire();
                stagiaire.setId(rs.getInt("id"));
                stagiaire.setUserId(rs.getInt("user_id"));
                stagiaire.setFirstName(rs.getString("first_name"));
                stagiaire.setLastName(rs.getString("last_name"));
                stagiaire.setCin(rs.getString("cin"));
                stagiaire.setDateOfBirth(rs.getDate("date_of_birth"));
                stagiaire.setUniversity(rs.getString("university"));
                stagiaire.setFieldOfStudy(rs.getString("field_of_study"));
                stagiaire.setStartDate(rs.getDate("start_date"));
                stagiaire.setEndDate(rs.getDate("end_date"));
                
                stagiaires.add(stagiaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stagiaires;
    }

    // Update operation
    public boolean updateStagiaire(Stagiaire stagiaire) {
        String sql = "UPDATE stagiaires SET first_name = ?, last_name = ?, cin = ?, " +
                     "date_of_birth = ?, university = ?, field_of_study = ?, " +
                     "start_date = ?, end_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, stagiaire.getFirstName());
            stmt.setString(2, stagiaire.getLastName());
            stmt.setString(3, stagiaire.getCin());
            stmt.setDate(4, new java.sql.Date(stagiaire.getDateOfBirth().getTime()));
            stmt.setString(5, stagiaire.getUniversity());
            stmt.setString(6, stagiaire.getFieldOfStudy());
            stmt.setDate(7, new java.sql.Date(stagiaire.getStartDate().getTime()));
            stmt.setDate(8, new java.sql.Date(stagiaire.getEndDate().getTime()));
            stmt.setInt(9, stagiaire.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete operation
    public boolean deleteStagiaire(int id) {
        String sql = "DELETE FROM stagiaires WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Additional utility methods
    public Stagiaire getStagiaireByUserId(int userId) {
        String sql = "SELECT * FROM stagiaires WHERE user_id = ?";
        Stagiaire stagiaire = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stagiaire = new Stagiaire();
                    stagiaire.setId(rs.getInt("id"));
                    stagiaire.setUserId(rs.getInt("user_id"));
                    stagiaire.setFirstName(rs.getString("first_name"));
                    stagiaire.setLastName(rs.getString("last_name"));
                    stagiaire.setCin(rs.getString("cin"));
                    stagiaire.setDateOfBirth(rs.getDate("date_of_birth"));
                    stagiaire.setUniversity(rs.getString("university"));
                    stagiaire.setFieldOfStudy(rs.getString("field_of_study"));
                    stagiaire.setStartDate(rs.getDate("start_date"));
                    stagiaire.setEndDate(rs.getDate("end_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stagiaire;
    }
    public int getNextUserId() {
    int nextId = 16; // Default starting ID
    
    try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
        ResultSet rs = stmt.executeQuery("SELECT MAX(user_id) FROM stagiaires");
        if (rs.next()) {
            nextId = rs.getInt(1) + 1; // Increment the max ID by 1
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return nextId;
}

}
