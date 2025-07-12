package com.dmcdesigns.d308_mobile.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private String excDate;
    private int vacationID;

    public Excursion(int excursionID, String excursionName, String excDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.excDate = excDate;
        this.vacationID = vacationID;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public String getExcDate(){
        return excDate;
    }

    public void setExcDate(String excDate){
        this.excDate = excDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
