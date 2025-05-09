/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionStagiaire.models;

/**
 *
 * @author agour
 */


import java.util.Date;

public class Internship {
    private int id;
    private int stagiaireId;
    private int supervisorId;
    private String title;
    private String description;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
    private String reportFilePath;
    private Double grade;
    private String feedback;
    private Date startDate;
    private Date endDate;
    
    // Constructors
    public Internship() {}
    // Simplified constructor for new internships
public Internship(int id, int stagiaireId, int supervisorId, String title, 
                 String description, String status, 
                 Date startDate, Date endDate) {
    this(id, stagiaireId, supervisorId, title, description, status,
         null, null, null, startDate, endDate);
}
    public Internship(int id, int stagiaireId, int supervisorId, String title, 
                     String description, String status, String reportFilePath, 
                     Double grade, String feedback, Date startDate, Date endDate) {
        this.id = id;
        this.stagiaireId = stagiaireId;
        this.supervisorId = supervisorId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.reportFilePath = reportFilePath;
        this.grade = grade;
        this.feedback = feedback;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStagiaireId() {
        return stagiaireId;
    }

    public void setStagiaireId(int stagiaireId) {
        this.stagiaireId = stagiaireId;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}