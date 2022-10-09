package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.CreateEventRequestData;

public class JsonStringToEventConverter {

    public CreateEventRequestData convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            CreateEventRequestData request = gson.fromJson(body, CreateEventRequestData.class);
            return request;
        } catch (Exception e) {
            throw new InvalidDataException("Event could not be deserialized");
        }
    }
}
