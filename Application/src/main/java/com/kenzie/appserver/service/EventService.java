package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.model.EventUserRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.model.EventRepository;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EventService {
    private EventRepository eventRepository;
    private EventUserRepository eventOrganizerRepository;
    private LambdaServiceClient lambdaServiceClient;

    public EventService(EventRepository eventRepository, LambdaServiceClient lambdaServiceClient, EventUserRepository eventOrganizerRepository) {
        this.eventRepository = eventRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.eventOrganizerRepository = eventOrganizerRepository;
    }

    public EventResponse getEventById(String id){
        Optional<EventRecord> record = eventRepository.findById(id);
        return record.map(this::recordToResponse).orElse(null);
    }

    /**
     *  Should we add check to see if Organizer is the one responsible for creating the updated event?
     */
    public EventResponse updateEventById(String id, String name, String date, User user, List<String> listOfUsers,
                                         String address, String description){

        Optional<EventRecord> eventExists = eventRepository.findById(id);
        if (eventExists.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer Not Found");
        }
//        if (eventOrganizerRepository.existsById(user.getId())){
            EventRecord eventRecord = eventExists.get();
            eventRecord.setName(name);
            eventRecord.setDate(date);
            eventRecord.setOrganizer(user);
            eventRecord.setListOfUsersAttending(listOfUsers);
            eventRecord.setAddress(address);
            eventRecord.setDescription(description);
            eventRepository.save(eventRecord);

            return recordToResponse(eventRecord);
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Organizer");
    }

    /**
     *  Do we want to make a check, to make sure that the user(organizer) is registered
     *  within the system? Would have to create a class of type Organizer that has the name/ id.
     */
    public EventResponse addNewEvent(CreateEventRequest createEventRequest){

        EventRecord eventRecord = new EventRecord();

        if (createEventRequest != null){
//            if (eventOrganizerRepository.existsById(createEventRequest.getUser().getId())) {
                eventRecord.setId(UUID.randomUUID().toString());
                eventRecord.setName(createEventRequest.getName());
                eventRecord.setDate(createEventRequest.getDate());
                eventRecord.setOrganizer(createEventRequest.getUser());
                eventRecord.setListOfUsersAttending(createEventRequest.getListOfUsersAttending());
                eventRecord.setAddress(createEventRequest.getAddress());
                eventRecord.setDescription(createEventRequest.getDescription());
                eventRepository.save(eventRecord);
            }
//        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateEventRequest was either null or id was invalid");
        }
        return recordToResponse(eventRecord);
    }

    public EventResponse recordToResponse(EventRecord eventRecord){

        if (eventRecord == null){
            return null;
        }
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(eventRecord.getId());
        eventResponse.setName(eventRecord.getName());
        eventResponse.setDate(eventRecord.getDate());
        eventResponse.setUser(eventRecord.getUser());
        eventResponse.setListOfUsersAttending(Collections.singletonList(eventRecord.getListOfUsersAttending()));
        eventResponse.setAddress(eventRecord.getAddress());
        eventResponse.setDescription(eventRecord.getDescription());

        return eventResponse;
    }

}
