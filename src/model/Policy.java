/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Fikal Alif
 */
public class Policy implements Serializable{
    private int id;
    private String title;
    private String institution;
    private String category;
    private String date;
    private String status;
    private String description;
    private static final long serialVersionUID = 1L;

    public Policy(int id, String title, String institution, String category, String date, String status, String description) {
        this.id = id;
        this.title = title;
        this.institution = institution;
        this.category = category;
        this.date = date;
        this.status = status;
        this.description = description;
    }
    
    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getInstitution() { return institution; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
}
