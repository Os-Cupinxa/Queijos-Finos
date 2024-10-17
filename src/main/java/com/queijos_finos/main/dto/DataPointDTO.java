package com.queijos_finos.main.dto;

import java.util.List;

public class DataPointDTO {

    private List<Double> curData;
    private List<Double> pastData;
    private List<String> timeCurWeek;
    private List<String> timePastWeek;


    public DataPointDTO(List<Double> curData, List<Double> pastData, List<String> timeCurWeek, List<String> timePastWeek) {
        this.curData = curData;
        this.pastData = pastData;
        this.timeCurWeek = timeCurWeek;
        this.timePastWeek = timePastWeek;
    }

    public List<Double> getCurData() {
        return curData;
    }

    public void setCurData(List<Double> curData) {
        this.curData = curData;
    }

    public List<Double> getPastData() {
        return pastData;
    }

    public void setPastData(List<Double> pastData) {
        this.pastData = pastData;
    }

    public List<String> getTimeCurWeek() {
        return timeCurWeek;
    }

    public void setTimeCurWeek(List<String> timeCurWeek) {
        this.timeCurWeek = timeCurWeek;
    }

    public List<String> getTimePastWeek() {
        return timePastWeek;
    }

    public void setTimePastWeek(List<String> timePastWeek) {
        this.timePastWeek = timePastWeek;
    }
}
