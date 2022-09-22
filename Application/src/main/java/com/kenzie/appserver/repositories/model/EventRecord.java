package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kenzie.appserver.service.model.User;

import java.util.List;

@DynamoDBTable(tableName = "events")
public class EventRecord {
    private String id;
    private String name;
    private String date;
    private User user;
    private List<String> listOfAttending;
    private String address;
    private String description;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDBTypeConverted(converter = UserTypeConverter.class)
    @DynamoDBAttribute(attributeName = "user")
    public User getUser() {
        return user;
    }

    @DynamoDBAttribute(attributeName = "user")
    public void setUser(User user) {
        this.user = user;
    }

    @DynamoDBAttribute(attributeName = "listOfAttending")
    public List<String> getListOfAttending() {
        return listOfAttending;
    }

    public void setListOfAttending(List<String> listOfAttending) {
        this.listOfAttending = listOfAttending;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
