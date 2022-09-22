package com.kenzie.appserver.controller;//package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.EventService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private EventQueryUtility eventQueryUtility;

    @BeforeAll
    public void setup(){ eventQueryUtility = new EventQueryUtility(mvc);}

    @Test
    public void getEventById_validId_isSuccessful() throws Exception {

        List<String> list = new ArrayList<>();
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());

        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.names().first().get());
        createEventRequest.setDate(LocalDate.now().toString());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.names().get(), mockNeat.emails().get()));
        createEventRequest.setListOfAttending(list);
        createEventRequest.setAddress(mockNeat.addresses().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        EventResponse eventResponse = eventService.addNewEvent(createEventRequest);

        // WHEN
        eventQueryUtility.eventControllerClient.getEventById(eventResponse.getId())
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("name")
                        .value(is(createEventRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(createEventRequest.getDate())))
                .andExpect(jsonPath("$.user.name")
                        .value(is(createEventRequest.getUser().getName())))
                .andExpect(jsonPath("listOfAttending")
                        .value(is(createEventRequest.getListOfAttending())))
                .andExpect(jsonPath("address")
                        .value(is(createEventRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(createEventRequest.getDescription())))
                .andExpect(status().isOk());
    }

    @Test
    public void addEvent_validRequest_isSuccessful() throws Exception {

        List<String> list = new ArrayList<>();
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());

//        User user = new User();
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setName(mockNeat.names().get());
//        createUserRequest.setEmail(mockNeat.emails().get());
//
//        UserResponse userResponse = userService.createUser(createUserRequest);
//
//        user.setId(userResponse.getId());
//        user.setName(userResponse.getName());
//        user.setEmail(userResponse.getEmail());

        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.names().first().get());
        createEventRequest.setDate(LocalDate.now().toString());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.names().get(), mockNeat.emails().get()));
        createEventRequest.setListOfAttending(list);
        createEventRequest.setAddress(mockNeat.addresses().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        // WHEN
        eventQueryUtility.eventControllerClient.addEvent(createEventRequest)
                // THEN
                .andExpect(jsonPath("name")
                        .value(is(createEventRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(createEventRequest.getDate())))
                .andExpect(jsonPath("$.user.name")
                        .value(is(createEventRequest.getUser().getName())))
                .andExpect(jsonPath("listOfAttending")
                        .value(is(createEventRequest.getListOfAttending())))
                .andExpect(jsonPath("address")
                        .value(is(createEventRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(createEventRequest.getDescription())))
                .andExpect(status().is2xxSuccessful());

        eventQueryUtility.eventControllerClient.deleteEvent(createEventRequest.getId());
    }

    @Test
    public void updateEvent_validRequest_isSuccessful() throws Exception {

        List<String> list = new ArrayList<>();
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());

        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.names().first().get());
        createEventRequest.setDate(LocalDate.now().toString());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.names().get(), mockNeat.emails().get()));
        createEventRequest.setListOfAttending(list);
        createEventRequest.setAddress(mockNeat.addresses().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        EventResponse eventResponse = eventService.addNewEvent(createEventRequest);

        // WHEN
        EventUpdateRequest updateRequest = new EventUpdateRequest();
        updateRequest.setId(eventResponse.getId());
        updateRequest.setName(mockNeat.names().first().get());
        updateRequest.setDate(LocalDate.now().toString());
        updateRequest.setUser(eventResponse.getUser());
        updateRequest.setListOfAttending(list);
        updateRequest.setAddress(mockNeat.addresses().get());
        updateRequest.setDescription(mockNeat.strings().get());

        eventQueryUtility.eventControllerClient.updateEvent(updateRequest)
                // THEN
                .andExpect(jsonPath("id")
                        .value(is(updateRequest.getId())))
                .andExpect(jsonPath("name")
                        .value(is(updateRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(updateRequest.getDate())))
                .andExpect(jsonPath("listOfAttending")
                        .value(is(updateRequest.getListOfAttending())))
                .andExpect(jsonPath("address")
                        .value(is(updateRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(updateRequest.getDescription())))
                .andExpect(status().isOk());

        eventQueryUtility.eventControllerClient.deleteEvent(createEventRequest.getId());
    }

    @Test
    public void deleteEvent_validId_isSuccessful() throws Exception {

        List<String> list = new ArrayList<>();
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());
        list.add(mockNeat.names().get());

        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.names().first().get());
        createEventRequest.setDate(LocalDate.now().toString());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.names().get(), mockNeat.emails().get()));
        createEventRequest.setListOfAttending(list);
        createEventRequest.setAddress(mockNeat.addresses().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        // WHEN
        EventResponse eventResponse = eventService.addNewEvent(createEventRequest);

       eventQueryUtility.eventControllerClient.deleteEvent(eventResponse.getId())
                .andExpect(status().isOk());

        eventQueryUtility.eventControllerClient.getEventById(eventResponse.getId())
                .andExpect(status().isNotFound());
    }


}