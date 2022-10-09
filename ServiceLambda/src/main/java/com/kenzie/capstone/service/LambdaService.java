package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.EventConverter;
import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

public class LambdaService {

    private EventDao eventDao;

    @Inject
    public LambdaService(EventDao eventDao){
        this.eventDao = eventDao;
    }

    public EventResponseData getEventById(String id){
        List<EventRecord> eventResponses = eventDao.getEventById(id);
        if(eventResponses.size() > 0){
            return new EventResponseData(eventResponses.get(0).getId(), eventResponses.get(0).getName(), eventResponses.get(0).getDate(), eventResponses.get(0).getUser(), eventResponses.get(0).getListOfAttending(), eventResponses.get(0).getAddress(), eventResponses.get(0).getDescription());
        }
        return null;
    }

    // this is called from the lambda in ServiceLambda
    public EventResponseData addEvent(CreateEventRequestData event) {
        if (event == null) {
            throw new InvalidDataException("Request must contain a valid event");
        }
        EventRecord record = EventConverter.fromRequestToRecord(event);
        eventDao.postNewEvent(record);
        return EventConverter.fromRecordToResponse(record);
    }

    public List<Event> getAllEvents() {
        return eventDao.getAllEvents().stream()
                .map(EventConverter::fromRecordToEvent)
                .collect(Collectors.toList());
    }

}
