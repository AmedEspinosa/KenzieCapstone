package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.kenzie.appserver.service.model.User;

public class UserTypeConverter implements DynamoDBTypeConverter<String, User> {

    @Override
    public String convert(User object) {
        User user = (User) object;
        String users = null;
        try {
            if (user != null) {
                users = String.format("%s x %s x %s", user.getId(), user.getName(),
                        user.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User unconvert(String s) {

        User user = new User();
        try {
            if (s != null && s.length() != 0) {
                String[] data = s.split("x");
                user.setId(data[0].trim());
                user.setName(data[1].trim());
                user.setEmail(data[2].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

