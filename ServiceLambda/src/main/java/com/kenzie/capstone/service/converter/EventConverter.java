package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.CreateEventRequestData;
import com.kenzie.capstone.service.model.Event;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.EventResponseData;

public class EventConverter {

    public static EventRecord fromRequestToRecord(CreateEventRequestData event) {
        EventRecord record = new EventRecord();
        record.setId(event.getId());
        record.setName(event.getName());
        record.setDate(event.getDate());
        record.setUser(event.getUser());
        record.setAddress(event.getAddress());
        record.setListOfAttending(event.getListOfAttending());
        record.setDescription(event.getDescription());
        return record;
    }

    public static EventResponseData fromRecordToResponse(EventRecord record) {
        EventResponseData response = new EventResponseData();
        response.setId(record.getId());
        response.setName(record.getName());
        response.setDate(record.getDate());
        response.setUser(record.getUser());
        response.setAddress(record.getAddress());
        response.setListOfAttending(record.getListOfAttending());
        response.setDescription(record.getDescription());
        return response;
    }

    public static Event fromRecordToEvent(EventRecord record) {
        Event event = new Event();
        event.setId(record.getId());
        event.setName(record.getName());
        event.setDate(record.getDate());
        event.setUser(record.getUser());
        event.setAddress(record.getAddress());
        event.setListOfUsersAttending(record.getListOfAttending());
        event.setDescription(record.getDescription());
        return event;
    }
}
