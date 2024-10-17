package com.queijos_finos.main.dto;

public class DataInsightDTO {

    private Integer active;
    private Integer activeInContemplation;
    private Integer dropout;

    public DataInsightDTO(Integer active, Integer activeInContemplation, Integer dropout) {
        this.active = active;
        this.activeInContemplation = activeInContemplation;
        this.dropout = dropout;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getActiveInContemplation() {
        return activeInContemplation;
    }

    public void setActiveInContemplation(Integer activeInContemplation) {
        this.activeInContemplation = activeInContemplation;
    }

    public Integer getDropout() {
        return dropout;
    }

    public void setDropout(Integer dropout) {
        this.dropout = dropout;
    }
}
