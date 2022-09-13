package com.kenzie.appserver.controller;//package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.model.Example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

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
class ExampleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    EventService eventService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private EventQueryUtility eventQueryUtility;

    @BeforeAll
    public void setup(){ eventQueryUtility = new EventQueryUtility(mvc);}

    @Test
    public void getEventById_validId_isSuccessful() throws Exception {

        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.strings().get());
        createEventRequest.setDate(mockNeat.strings().get());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get()));
        createEventRequest.setListOfUsersAttending(mock(List.class));
        createEventRequest.setAddress(mockNeat.strings().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        eventQueryUtility.eventControllerClient.addEvent(createEventRequest);

        // WHEN
        eventQueryUtility.eventControllerClient.getEventById(createEventRequest.getId())
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("id")
                        .value(is(createEventRequest.getId())))
                .andExpect(jsonPath("name")
                        .value(is(createEventRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(createEventRequest.getDate())))
                .andExpect(jsonPath("user")
                        .value(is(createEventRequest.getUser())))
                .andExpect(jsonPath("listOfUsersAttending")
                        .value(is(createEventRequest.getListOfUsersAttending())))
                .andExpect(jsonPath("address")
                        .value(is(createEventRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(createEventRequest.getDescription())))
                .andExpect(status().isOk());
    }

    @Test
    public void addEvent_validRequest_isSuccessful() throws Exception {

        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.strings().get());
        createEventRequest.setDate(mockNeat.strings().get());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get()));
        createEventRequest.setListOfUsersAttending(mock(List.class));
        createEventRequest.setAddress(mockNeat.strings().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        // WHEN
        eventQueryUtility.eventControllerClient.addEvent(createEventRequest)
                // THEN
                .andExpect(jsonPath("id")
                        .value(is(createEventRequest.getId())))
                .andExpect(jsonPath("name")
                        .value(is(createEventRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(createEventRequest.getDate())))
                .andExpect(jsonPath("user")
                        .value(is(createEventRequest.getUser())))
                .andExpect(jsonPath("listOfUsersAttending")
                        .value(is(createEventRequest.getListOfUsersAttending())))
                .andExpect(jsonPath("address")
                        .value(is(createEventRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(createEventRequest.getDescription())))
                .andExpect(status().isOk());

        eventQueryUtility.eventControllerClient.deleteEvent(createEventRequest.getId());
    }

    @Test
    public void updateEvent_validRequest_isSuccessful() throws Exception {

        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.strings().get());
        createEventRequest.setDate(mockNeat.strings().get());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get()));
        createEventRequest.setListOfUsersAttending(mock(List.class));
        createEventRequest.setAddress(mockNeat.strings().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        eventQueryUtility.eventControllerClient.addEvent(createEventRequest);

        // WHEN
        EventUpdateRequest updateRequest = new EventUpdateRequest();
        updateRequest.setName(mockNeat.strings().get());
        updateRequest.setDate(mockNeat.strings().get());
        updateRequest.setListOfUsersAttending(mock(List.class));
        updateRequest.setAddress(mockNeat.strings().get());
        updateRequest.setDescription(mockNeat.strings().get());

        eventQueryUtility.eventControllerClient.updateEvent(updateRequest);

        eventQueryUtility.eventControllerClient.getEventById(createEventRequest.getId())
                // THEN
                .andExpect(jsonPath("id")
                        .value(is(updateRequest.getId())))
                .andExpect(jsonPath("name")
                        .value(is(updateRequest.getName())))
                .andExpect(jsonPath("date")
                        .value(is(updateRequest.getDate())))
                .andExpect(jsonPath("user")
                        .value(is(updateRequest.getUser())))
                .andExpect(jsonPath("listOfUsersAttending")
                        .value(is(updateRequest.getListOfUsersAttending())))
                .andExpect(jsonPath("address")
                        .value(is(updateRequest.getAddress())))
                .andExpect(jsonPath("description")
                        .value(is(updateRequest.getDescription())))
                .andExpect(status().isOk());

        eventQueryUtility.eventControllerClient.deleteEvent(createEventRequest.getId());
    }


    @Test
    public void deleteEvent_validId_isSuccessful() throws Exception {
        // GIVEN
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName(mockNeat.strings().get());
        createEventRequest.setDate(mockNeat.strings().get());
        createEventRequest.setUser(new User(UUID.randomUUID().toString(), mockNeat.strings().get(), mockNeat.strings().get()));
        createEventRequest.setListOfUsersAttending(mock(List.class));
        createEventRequest.setAddress(mockNeat.strings().get());
        createEventRequest.setDescription(mockNeat.strings().get());

        // WHEN
        eventQueryUtility.eventControllerClient.addEvent(createEventRequest);

        eventQueryUtility.eventControllerClient.deleteEvent(createEventRequest.getId())
                // THEN
                .andExpect(status().isOk());

        assertThrows(NestedServletException.class, () -> {
            eventQueryUtility.eventControllerClient.getEventById(createEventRequest.getId());
        });
    }


}