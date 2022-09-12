package com.kenzie.appserver.controller.model;//package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class EventUpdateRequest {

    @NotEmpty
    @JsonProperty("id")
    private String id;

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("date")
    private String date;

    @NotEmpty
    @JsonProperty("organizer")
    private String organizer;

    @NotEmpty
    @JsonProperty("listOfUsersAttending")
    private String listOfUsersAttending;

    @NotEmpty
    @JsonProperty("address")
    private String address;

    @NotEmpty
    @JsonProperty("description")
    private String description;

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

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getListOfUsersAttending() {
        return listOfUsersAttending;
    }

    public void setListOfUsersAttending(String listOfUsersAttending) {
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
