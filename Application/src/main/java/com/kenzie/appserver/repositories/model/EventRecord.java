package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.User;

import java.util.List;

@DynamoDBTable(tableName = "Events")
public class EventRecord {
    private String id;
    private String name;
    private String date;
    private User user;
    private List<String> listOfAttending;
    private String address;
    private String description;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDBAttribute(attributeName = "User")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @DynamoDBAttribute(attributeName = "ListOfAttending")
    public List<String> getListOfAttending() {
        return listOfAttending;
    }

    public void setListOfAttending(List<String> listOfAttending) {
        this.listOfAttending = listOfAttending;
    }

    @DynamoDBAttribute(attributeName = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
