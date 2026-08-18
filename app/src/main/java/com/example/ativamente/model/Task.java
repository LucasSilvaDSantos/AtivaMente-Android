package com.example.ativamente.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tasks")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int notificationId;
    private String title;
    private String description;
    private String date;
    private String time;
    private boolean isCompleted;
    private String userId;
    private boolean isRoutine;
    private String daysOfWeek; // Ex: "1,3,5" para Seg, Qua, Sex
    private String excludedDates = "";

    public Task() {
    }

    public Task(String title, String description, String date, String time, boolean isCompleted, String userId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.isRoutine = false;
        this.daysOfWeek = "";
        this.excludedDates = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRoutine() {
        return isRoutine;
    }

    public void setRoutine(boolean routine) {
        isRoutine = routine;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getExcludedDates() {
        return excludedDates;
    }

    public void setExcludedDates(String excludedDates) {
        this.excludedDates = excludedDates;
    }
}
