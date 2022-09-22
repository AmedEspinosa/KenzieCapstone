package com.kenzie.appserver.service.model;

import org.apache.catalina.User;

import java.util.List;

public class Event {
    private String id;
    private String name;
    private String date;
    private User user;
    private List<String> listOfUsersAttending;
    private String address;
    private String description;

    public Event(String id, String name, String date, User user, List<String> listOfUsersAttending, String address, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.user = user;
        this.listOfUsersAttending = listOfUsersAttending;
        this.address = address;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getListOfUsersAttending() {
        return listOfUsersAttending;
    }

    public void setListOfUsersAttending(List<String> listOfUsersAttending) {
        this.listOfUsersAttending = listOfUsersAttending;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}