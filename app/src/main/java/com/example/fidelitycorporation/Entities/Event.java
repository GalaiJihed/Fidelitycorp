package com.example.fidelitycorporation.Entities;

import java.util.ArrayList;

public class Event {
    private int Id;
    private String eventName;
    private String eventType;
    private String eventDate;
    public static ArrayList<Event> events = new ArrayList<>();


    public Event() {
    }

    public Event(int id, String eventName, String eventType, String eventDate) {
        Id = id;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return "Event{" +
                "Id=" + Id +
                ", eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate='" + eventDate + '\'' +
                '}';
    }
}
