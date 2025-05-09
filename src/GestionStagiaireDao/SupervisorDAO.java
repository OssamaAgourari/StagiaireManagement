/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaireDao;
import GestionStagiaire.models.Supervisor;
import gestionstagiares.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agour
 */
public class SupervisorDAO {
    public boolean addSupervisor(Supervisor supervisor) {
        String sql = "INSERT INTO supervisors (user_id, first_name, last_name, department, position) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, supervisor.getUserId());
            stmt.setString(2, supervisor.getFirstName());
            stmt.setString(3, supervisor.getLastName());
            stmt.setString(4, supervisor.getDepartment());
            stmt.setString(5, supervisor.getPosition());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        supervisor.setId(rs.getInt(1));
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
    public Supervisor getSupervisorById(int id) {
        String sql = "SELECT * FROM supervisors WHERE id = ?";
        Supervisor supervisor = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    supervisor = new Supervisor();
                    supervisor.setId(rs.getInt("id"));
                    supervisor.setUserId(rs.getInt("user_id"));
                    supervisor.setFirstName(rs.getString("first_name"));
                    supervisor.setLastName(rs.getString("last_name"));
                    supervisor.setDepartment(rs.getString("department"));
                    supervisor.setPosition(rs.getString("position"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supervisor;
    }

    public List<Supervisor> getAllSupervisors() {
        List<Supervisor> supervisors = new ArrayList<>();
        String sql = "SELECT * FROM supervisors ORDER BY last_name, first_name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Supervisor supervisor = new Supervisor();
                supervisor.setId(rs.getInt("id"));
                supervisor.setUserId(rs.getInt("user_id"));
                supervisor.setFirstName(rs.getString("first_name"));
                supervisor.setLastName(rs.getString("last_name"));
                supervisor.setDepartment(rs.getString("department"));
                supervisor.setPosition(rs.getString("position"));
                
                supervisors.add(supervisor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supervisors;
    }

    // Update operation
    public boolean updateSupervisor(Supervisor supervisor) {
        String sql = "UPDATE supervisors SET first_name = ?, last_name = ?, " +
                     "department = ?, position = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, supervisor.getFirstName());
            stmt.setString(2, supervisor.getLastName());
            stmt.setString(3, supervisor.getDepartment());
            stmt.setString(4, supervisor.getPosition());
            stmt.setInt(5, supervisor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete operation
    public boolean deleteSupervisor(int id) {
        String sql = "DELETE FROM supervisors WHERE id = ?";
        
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
    public Supervisor getSupervisorByUserId(int userId) {
        String sql = "SELECT * FROM supervisors WHERE user_id = ?";
        Supervisor supervisor = null;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    supervisor = new Supervisor();
                    supervisor.setId(rs.getInt("id"));
                    supervisor.setUserId(rs.getInt("user_id"));
                    supervisor.setFirstName(rs.getString("first_name"));
                    supervisor.setLastName(rs.getString("last_name"));
                    supervisor.setDepartment(rs.getString("department"));
                    supervisor.setPosition(rs.getString("position"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supervisor;
    }

    public List<Supervisor> getSupervisorsByDepartment(String department) {
        List<Supervisor> supervisors = new ArrayList<>();
        String sql = "SELECT * FROM supervisors WHERE department = ? ORDER BY last_name, first_name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, department);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Supervisor supervisor = new Supervisor();
                    supervisor.setId(rs.getInt("id"));
                    supervisor.setUserId(rs.getInt("user_id"));
                    supervisor.setFirstName(rs.getString("first_name"));
                    supervisor.setLastName(rs.getString("last_name"));
                    supervisor.setDepartment(rs.getString("department"));
                    supervisor.setPosition(rs.getString("position"));
                    
                    supervisors.add(supervisor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supervisors;
    }
}
