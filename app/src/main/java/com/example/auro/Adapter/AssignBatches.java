package com.example.auro.Adapter;

public class AssignBatches {

    public AssignBatches(){

    }

    public String noOfBatches, projectManager, std, batchesCreated;



    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public void setBatchesCreated(String batchesCreated) {
        this.batchesCreated = batchesCreated;
    }

    public void setNoOfBatches(String noOfBatches) {
        this.noOfBatches = noOfBatches;
    }

    public String getNoOfBatches() {
        return noOfBatches;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public String getStd() {
        return std;
    }

    public String getBatchesCreated() {
        return batchesCreated;
    }
}
