package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.service.model.Event;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.EventResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheStore cache;

    public EventService(EventRepository eventRepository, LambdaServiceClient lambdaServiceClient, EventUserRepository eventUserRepository, CacheStore cache) {
        this.eventRepository = eventRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.eventUserRepository = eventUserRepository;
        this.cache = cache;
    }

    public EventResponse getEventById(String id){

        EventResponseData lambdaResponse = lambdaServiceClient.getEventById(id);

        Optional<EventRecord> record = eventRepository.findById(id);

        EventResponse recordToResponses = record.map(this::recordToResponse)
                                                .orElse(null);
        if(record.isPresent()){
            cache.add(record.get().getId(), record);
        }
        return recordToResponses;
    }

    /**
     *
     * @param eventUpdateRequest - checks to make sure that the user who has created the event is the
     *                           only one able to make an update to that event.
     */
    public EventResponse updateEventById(EventUpdateRequest eventUpdateRequest){

        Optional<EventRecord> event = eventRepository.findById(eventUpdateRequest.getId());
        EventRecord eventRecord;
        if (event.isPresent()){
            if (event.get().getUser().getId().equals(eventUpdateRequest.getUser().getId())) {
                eventRecord = event.get();
                eventRecord.setId(eventUpdateRequest.getId());
                eventRecord.setName(eventUpdateRequest.getName());
                eventRecord.setDate(eventUpdateRequest.getDate());
                eventRecord.setListOfAttending(eventUpdateRequest.getListOfAttending());
                eventRecord.setAddress(eventUpdateRequest.getAddress());
                eventRecord.setDescription(eventUpdateRequest.getDescription());
                eventRepository.save(eventRecord);
                cache.evict(eventRecord.getId());

                return recordToResponse(eventRecord);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User request.");
    }

    /**
     *  @param createEventRequest - checks to make sure that the user is a valid user before
     *                            allowing Event to be created.
     */
    public EventResponse addNewEvent(CreateEventRequest createEventRequest){

        EventRecord eventRecord = new EventRecord();

        if (createEventRequest != null){
//            if (eventUserRepository.existsById(createEventRequest.getUser().getId())) {
                eventRecord.setId(UUID.randomUUID().toString());
                eventRecord.setName(createEventRequest.getName());
                eventRecord.setDate(createEventRequest.getDate());
                eventRecord.setUser(createEventRequest.getUser());
                eventRecord.setListOfAttending(createEventRequest.getListOfAttending());
                eventRecord.setAddress(createEventRequest.getAddress());
                eventRecord.setDescription(createEventRequest.getDescription());
                eventRepository.save(eventRecord);

                return recordToResponse(eventRecord);
            }
//        }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreateEventRequest was either null or id was invalid");
    }

    public void deleteEvent(String eventId){
        if (eventId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is Empty");
        }
        if (!eventRepository.existsById(eventId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id does not exist in the repository");
        }
            eventRepository.deleteById(eventId);
            cache.evict(eventId);
    }

    public List<EventResponse> getAllEvents(){

        Iterable<EventRecord> responseOfRecords = eventRepository.findAll();
        List<Event> events = new ArrayList<>();

        for(EventRecord record : responseOfRecords) {
            events.add(new Event(record.getId(),
                    record.getName(),
                    record.getDate(),
                    record.getUser(),
                    record.getListOfAttending(),
                    record.getAddress(),
                    record.getDescription()));
        }

        return events.stream().map(this::eventToResponse).collect(Collectors.toList());
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
        eventResponse.setListOfAttending(eventRecord.getListOfAttending());
        eventResponse.setAddress(eventRecord.getAddress());
        eventResponse.setDescription(eventRecord.getDescription());

        return eventResponse;
    }

    public EventResponse eventToResponse(Event event){

        if (event == null){
            return null;
        }
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setName(event.getName());
        eventResponse.setDate(event.getDate());
        eventResponse.setUser(event.getUser());
        eventResponse.setListOfAttending(event.getListOfUsersAttending());
        eventResponse.setAddress(event.getAddress());
        eventResponse.setDescription(event.getDescription());

        return eventResponse;
    }

}
