package com.kenzie.appserver.controller.model;//package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateEventRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("date")
    private String date;

    @NotEmpty
    @JsonProperty("organizer")
    private String organizer;

    @JsonProperty("listOfUsersAttending")
    private List<String> listOfUsersAttending;

    @NotEmpty
    @JsonProperty("address")
    private String address;

    @NotEmpty
    @JsonProperty("description")
    private String description;


    public CreateEventRequest(){}

    public CreateEventRequest(String name, String date, String organizer, List<String> getListOfUsersAttending,
                              String address, String description){
        this.name = name;
        this.date = date;
        this.organizer = organizer;
        this.listOfUsersAttending = getListOfUsersAttending;
        this.address = address;
        this.description = description;
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
