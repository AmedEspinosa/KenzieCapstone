package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.service.model.Customer;
import com.kenzie.appserver.service.model.Event;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.CreateEventRequestData;
import com.kenzie.capstone.service.model.EventResponseData;
import com.kenzie.appserver.service.model.User;
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

        EventResponse recordToResponses = lambdaDataToResponse(lambdaResponse);


        if(lambdaResponse != null){
            cache.addToCash(lambdaResponse.getId(), recordToResponses);
            return recordToResponses;
        } else {
            return null;
            }
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

        List<com.kenzie.capstone.service.model.Customer> listOfUsersAttendingOnCreate = new ArrayList<>();
        for(int i = 0; i < createEventRequest.getListOfAttending().size(); i++){
            com.kenzie.capstone.service.model.Customer newCustomer = new com.kenzie.capstone.service.model.Customer();
            newCustomer.setId(createEventRequest.getListOfAttending().get(i).getId());
            newCustomer.setName(createEventRequest.getListOfAttending().get(i).getName());
            newCustomer.setEmail(createEventRequest.getListOfAttending().get(i).getEmail());
            listOfUsersAttendingOnCreate.add(newCustomer);
        }

        EventResponseData lambdaResponse = lambdaServiceClient.postNewEvent(new CreateEventRequestData(createEventRequest.getId(), createEventRequest.getName(), createEventRequest.getDate(), new com.kenzie.capstone.service.model.User(createEventRequest.getUser().getId(), createEventRequest.getUser().getName(), createEventRequest.getUser().getEmail()), listOfUsersAttendingOnCreate, createEventRequest.getAddress(), createEventRequest.getDescription()));
        System.out.println("We Are creating the event in the event service");

        EventResponse recordToResponses = lambdaDataToResponse(lambdaResponse);
        if(lambdaResponse.toString() == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The lambda did not work response was an empty string");
        } else{
            return recordToResponses;
        }

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
        System.out.println("Before Lambda");
        List<EventResponseData> allOfEvents = this.lambdaServiceClient.getAllEvents();
        System.out.println("AFter lambda");
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

    public EventResponse lambdaDataToResponse(EventResponseData eventRecord){

        if(eventRecord == null){
            return null;
        }

        List<Customer> customerListAttending = new ArrayList<>();
        for (int i = 0; i < eventRecord.getListOfAttending().size(); i++){
            Customer newCustomer = new Customer();
            newCustomer.setId(eventRecord.getListOfAttending().get(i).getId());
            newCustomer.setName(eventRecord.getListOfAttending().get(i).getName());
            newCustomer.setEmail(eventRecord.getListOfAttending().get(i).getEmail());
            customerListAttending.add(newCustomer);
        }

        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(eventRecord.getId());
        eventResponse.setName(eventRecord.getName());
        eventResponse.setDate(eventRecord.getDate());
        eventResponse.setUser(new User(eventRecord.getUser().getId(), eventRecord.getUser().getName(), eventRecord.getUser().getEmail()));
        eventResponse.setListOfAttending(customerListAttending);
        eventResponse.setAddress(eventRecord.getAddress());
        eventResponse.setDescription(eventRecord.getDescription());

        return eventResponse;
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
