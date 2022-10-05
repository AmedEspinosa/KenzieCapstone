package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Customer;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private EventService eventService;
    private LambdaServiceClient lambdaServiceClient;
    private CacheStore cacheStore;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @BeforeEach
    void setup() {
        eventRepository = mock(EventRepository.class);
        eventUserRepository = mock(EventUserRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheStore = mock(CacheStore.class);
        eventService = new EventService(eventRepository, lambdaServiceClient, eventUserRepository, cacheStore);
    }
    /** ------------------------------------------------------------------------
     *  eventService.getEventById
     *  ------------------------------------------------------------------------ **/
    @Test
    void getEventById() {
        // GIVEN
        String id = randomUUID().toString();
        List<Customer> usersAttending = mock(List.class);
        User user = new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        EventRecord record = new EventRecord();
        record.setId(id);
        record.setName(mockNeat.strings().get());
        record.setUser(user);
        record.setListOfAttending(usersAttending);
        record.setAddress(mockNeat.strings().get());
        record.setDescription(mockNeat.strings().get());
        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.of(record));
        when(cacheStore.get(record.getId())).thenReturn(Optional.of(record));
        Optional<EventRecord> eventResponse = eventRepository.findById(id);
        // THEN
        Assertions.assertNotNull(eventResponse, "The event is returned");
        Assertions.assertEquals(record.getId(), eventResponse.get().getId(), "The id matches");
        Assertions.assertEquals(record.getName(), eventResponse.get().getName(), "The name matches");
    }
    /** ------------------------------------------------------------------------
     *  eventService.addNewEvent
     *  ------------------------------------------------------------------------ **/
    @Test
    void addNewEvent() {

        String eventName = mockNeat.strings().get();
        User user = new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        CreateEventRequest request = new CreateEventRequest();
        request.setName(eventName);
        request.setDate(LocalDate.now().toString());
        request.setUser(user);
        request.setListOfAttending(mock(List.class));
        request.setDescription(mockNeat.strings().get());

        ArgumentCaptor<EventRecord> eventRecordCaptor = ArgumentCaptor.forClass(EventRecord.class);
        // WHEN
        when(eventUserRepository.existsById(request.getUser().getId())).thenReturn(true);
        EventResponse eventResponse = eventService.addNewEvent(request);
        // THEN
        Assertions.assertNotNull(eventResponse);
        verify(eventRepository).save(eventRecordCaptor.capture());

        EventRecord record = eventRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The event record is returned");
        Assertions.assertNotNull(record.getId(), "The event id exists");
        Assertions.assertEquals(record.getName(), eventName, "The event name matches");
        Assertions.assertEquals(record.getDate(), request.getDate(), "Dates match");
        Assertions.assertEquals(record.getListOfAttending(), request.getListOfAttending(), "Lists match");
        Assertions.assertEquals(record.getAddress(), request.getAddress(), "Address match");
        Assertions.assertEquals(record.getDescription(), request.getDescription(), "Descriptions match");
    }

    @Test
    void addNewEvent_createEventRequest_isNull_throws_exception() {

        CreateEventRequest createEventRequest = null;
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> eventService.addNewEvent(createEventRequest));
        // THEN
        try {
            verify(eventRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }
    /** ------------------------------------------------------------------------
     *  eventService.updateEventById
     *  ------------------------------------------------------------------------ **/
    @Test
    void updateEventById() {

        String eventId = randomUUID().toString();
        User user = new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        EventRecord oldEventRecord = new EventRecord();
        oldEventRecord.setId(eventId);
        oldEventRecord.setName(mockNeat.strings().get());
        oldEventRecord.setDate(LocalDate.now().minusDays(2).toString());
        oldEventRecord.setUser(user);
        oldEventRecord.setListOfAttending(mock(List.class));
        oldEventRecord.setAddress(mockNeat.strings().get());
        oldEventRecord.setDescription(mockNeat.strings().get());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(oldEventRecord));
        ArgumentCaptor<EventRecord> eventRecordCaptor = ArgumentCaptor.forClass(EventRecord.class);

        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();
        eventUpdateRequest.setId(oldEventRecord.getId());
        eventUpdateRequest.setName(mockNeat.names().get());
        eventUpdateRequest.setDate(LocalDate.now().toString());
        eventUpdateRequest.setUser(oldEventRecord.getUser());
        eventUpdateRequest.setListOfAttending(mock(List.class));
        eventUpdateRequest.setAddress(mockNeat.addresses().get());
        eventUpdateRequest.setDescription(mockNeat.departments().get());

        eventService.updateEventById(eventUpdateRequest);
        verify(eventRepository).save(eventRecordCaptor.capture());

        EventRecord record = eventRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The event record has been returned");
        Assertions.assertEquals(record.getId(), eventUpdateRequest.getId(), "The event id matches");
        Assertions.assertEquals(record.getName(), eventUpdateRequest.getName(), "The event name matches");
        Assertions.assertEquals(record.getDate(), eventUpdateRequest.getDate(), "The event date has been changed");
        Assertions.assertEquals(record.getListOfAttending(), eventUpdateRequest.getListOfAttending(), "Lists match");
        Assertions.assertEquals(record.getAddress(), eventUpdateRequest.getAddress(), "Both of the address matches");
        Assertions.assertEquals(record.getDescription(), eventUpdateRequest.getDescription(), "The descriptions match");
    }

    @Test
    void updateEventById_idIsNull_throws_responseStatusException() {
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();
        eventUpdateRequest.setId(UUID.randomUUID().toString());

        when(eventRepository.findById(eventUpdateRequest.getId())).thenReturn(Optional.empty());
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> eventService.updateEventById(eventUpdateRequest));
        // THEN
        try {
            verify(eventRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }
    /** ------------------------------------------------------------------------
     *  eventService.deleteEvent
     *  ------------------------------------------------------------------------ **/
    @Test
    void deleteEvent(){

        User user = new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        CreateEventRequest request = new CreateEventRequest();
        request.setName(mockNeat.strings().get());
        request.setDate(LocalDate.now().toString());
        request.setUser(user);
        request.setListOfAttending(mock(List.class));
        request.setAddress(mockNeat.addresses().get());
        request.setDescription(mockNeat.strings().get());

        EventResponse eventResponse = eventService.addNewEvent(request);
        when(eventRepository.existsById(eventResponse.getId())).thenReturn(true);

        eventService.deleteEvent(eventResponse.getId());
        verify(eventRepository).deleteById(eventResponse.getId());
    }

    @Test
    void deleteEvent_eventId_isEmpty_throws_exception() {

        String eventId = "";
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> eventService.deleteEvent(eventId));
        // THEN
        try {
            verify(eventRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }

    @Test
    void deleteEvent_eventId_doesNotExist_throws_exception() {

        String eventId = randomUUID().toString();
        when(eventRepository.existsById(eventId)).thenReturn(false);
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> eventService.deleteEvent(eventId));
        // THEN
        try {
            verify(eventRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }

    /** ------------------------------------------------------------------------
     *  eventService.getAllEvents
     *  ------------------------------------------------------------------------ **/

    @Test
    void getAllEvents_Successful(){

        Date dateOfToday = new Date();
        User user = new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        Customer userA1 = new Customer(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());
        Customer userA2 = new Customer(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());
        Customer userA3 = new Customer(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get());

        List<Customer> listOfUsersAttending = new ArrayList<>();
        listOfUsersAttending.add(userA1);
        listOfUsersAttending.add(userA2);
        listOfUsersAttending.add(userA3);

        //GIVEN
        EventRecord event1 = new EventRecord();
        event1.setId(randomUUID().toString());
        event1.setName("Event 1");
        event1.setDate(dateOfToday.toString());
        event1.setUser(user);
        event1.setListOfAttending(listOfUsersAttending);
        event1.setAddress("Event Address");
        event1.setDescription("Event Description");

        EventRecord event2 = new EventRecord();
        event1.setId(randomUUID().toString());
        event1.setName("Event 2");
        event1.setDate(dateOfToday.toString());
        event1.setUser(user);
        event1.setListOfAttending(listOfUsersAttending);
        event1.setAddress("Event Address 2");
        event1.setDescription("Event Description 2");

        List<EventRecord> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        when(eventRepository.findAll()).thenReturn(events);

        List<EventResponse> responses = eventService.getAllEvents();

        Assertions.assertNotNull(responses, "The List of Events is returned");
        Assertions.assertEquals(2, responses.size(), "There were two events returned");

        for (EventResponse event : responses) {
            if (event.getId() == event1.getId()) {
                Assertions.assertEquals(event1.getId(), event.getId(), "The event id matches");
                Assertions.assertEquals(event1.getName(), event.getName(), "The event name matches");
                Assertions.assertEquals(event1.getDate(), event.getDate(), "The event date matches");
                Assertions.assertEquals(event1.getUser(), event.getUser(), "The event user matches");
                Assertions.assertEquals(event1.getListOfAttending(), event.getListOfAttending(), "The event list of users match");
                Assertions.assertEquals(event1.getAddress(), event.getAddress(), "The event address matches");
                Assertions.assertEquals(event1.getDescription(), event.getDescription(), "The event description matches");
            } else if (event.getId() == event2.getId()) {
                Assertions.assertEquals(event2.getId(), event.getId(), "The event id matches");
                Assertions.assertEquals(event2.getName(), event.getName(), "The event name matches");
                Assertions.assertEquals(event2.getDate(), event.getDate(), "The event date matches");
                Assertions.assertEquals(event2.getListOfAttending(), event.getListOfAttending(), "The event list of users match");
                Assertions.assertEquals(event2.getUser(), event.getUser(), "The event user matches");
                Assertions.assertEquals(event2.getAddress(), event.getAddress(), "The event address matches");
                Assertions.assertEquals(event2.getDescription(), event.getDescription(), "The event description matches");
            } else {
                Assertions.assertTrue(false, "EventResponse returned that was not in the records!");
            }
        }
    }

}
