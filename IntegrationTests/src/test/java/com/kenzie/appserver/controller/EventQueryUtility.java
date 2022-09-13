package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class EventQueryUtility {

    public EventControllerClient eventControllerClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventQueryUtility(MockMvc mvc) {
        this.mvc = mvc;
        this.eventControllerClient = new EventControllerClient();
    }

    public class EventControllerClient {
        public ResultActions getEventById(String id) throws Exception {
            return mvc.perform(get("/events/{id}", id)
                    .accept(MediaType.APPLICATION_JSON));
        }

        public ResultActions getAllEvents() throws Exception {
            return mvc.perform(get("/events/all")
                    .accept(MediaType.APPLICATION_JSON));
        }

        public ResultActions addEvent(CreateEventRequest createEventRequest) throws Exception {
            return mvc.perform(post("/events/")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(createEventRequest)));
        }

        public ResultActions updateEvent(EventUpdateRequest eventUpdateRequest) throws Exception {
            return mvc.perform(put("/events/")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(eventUpdateRequest)));
        }

        public ResultActions deleteEvent(String eventId) throws Exception {
            return mvc.perform(delete("/rsvp/{name}", eventId)
                    .accept(MediaType.APPLICATION_JSON));
        }
    }

    }

