package com.example.auro.Adapter;

public class UserDetails {

    public UserDetails(){

    }
    public String name;
    public String pass;
    public String designation;
    public String reporting;

    public void setReporting(String reporting) {
        this.reporting = reporting;
    }

    public String getReporting() {
        return reporting;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getDesignation() {
        return designation;
    }
}
