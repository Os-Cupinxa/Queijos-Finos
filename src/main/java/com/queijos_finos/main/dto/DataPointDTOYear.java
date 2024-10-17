package com.queijos_finos.main.dto;

import java.util.List;

public class DataPointDTOYear {

    private List<Double> curData;
    private List<Double> pastData;
    private List<String> time;


    public DataPointDTOYear(List<Double> curData, List<Double> pastData, List<String> time) {
        this.curData = curData;
        this.pastData = pastData;
        this.time = time;
    }

    public List<Double> getCurYear() {
        return curData;
    }

    public void setCurYear(List<Double> curData) {
        this.curData = curData;
    }

    public List<Double> getPastYear() {
        return pastData;
    }

    public void setPastYear(List<Double> pastData) {
        this.pastData = pastData;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
