package com.example.pedometer;

public class HistoryItems {
    private String textDate;
    private String textTime;
    private String textSteps;

    public HistoryItems(String textDate, String textTime, String textSteps){
        this.textDate = textDate;
        this.textTime = textTime;
        this.textSteps = textSteps;
    }

    public String getTextDate(){
        return textDate;
    }

    public String getTextTime(){
        return textTime;
    }

    public String getTextSteps(){
        return textSteps;
    }
}
