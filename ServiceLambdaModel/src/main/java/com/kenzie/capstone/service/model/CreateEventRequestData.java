package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateEventRequestData {
    private String id;
    private String name;
    private String date;
    private User user;
    private List<Customer> listOfAttending;
    private String address;
    private String description;

    public CreateEventRequestData(){};

    public CreateEventRequestData(String id, String name, String date, User user, List<Customer> ListOfAttending,
                              String address, String description){
        this.id = "1";
        this.name = name;
        this.date = date;
        this.user = user;
        this.listOfAttending = ListOfAttending;
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

    public List<Customer> getListOfAttending() {
        return listOfAttending;
    }

    public void setListOfAttending(List<Customer> listOfAttending) {
        this.listOfAttending = listOfAttending;
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
