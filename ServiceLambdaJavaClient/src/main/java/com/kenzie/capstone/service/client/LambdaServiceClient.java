package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.CreateEventRequestData;
import com.kenzie.capstone.service.model.EventResponseData;
import com.kenzie.capstone.service.model.ExampleData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LambdaServiceClient {

    private static final String GET_EVENT_BY_ID_ENDPOINT = "events/{id}";
    private static final String POST_EVENT_ENDPOINT = "events";
    private static final String GET_ALL_EVENTS_ENDPOINT = "events/all";

    private ObjectMapper mapper;


    // make endpoints that match the controller path

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public EventResponseData getEventById(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EVENT_BY_ID_ENDPOINT.replace("{id}", id));
        EventResponseData eventResponseData;
        try {
            eventResponseData = mapper.readValue(response, EventResponseData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponseData;
    }

    // It is commented out in the EventService for a mismatch constructor type issue
    public EventResponseData postNewEvent(CreateEventRequestData createEventRequest) {
        createEventRequest.setId(UUID.randomUUID().toString());
        EndpointUtility endpointUtility = new EndpointUtility();

        String request;
        try {
            request = mapper.writeValueAsString(createEventRequest);
            System.out.println(request);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        System.out.println("Request was deserialized");
        System.out.println(POST_EVENT_ENDPOINT);
        System.out.println(request);
        String response = endpointUtility.postEndpoint(POST_EVENT_ENDPOINT, request);
        EventResponseData eventResponseData;
        try {
            eventResponseData = mapper.readValue(response, EventResponseData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponseData;
    }

    public List<EventResponseData> getAllEvents() {
        System.out.println("GetAllEvents is hit in lambdaserviceclient");
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getAllEndpoint(GET_ALL_EVENTS_ENDPOINT);
        System.out.println("This is the response of lists");
        System.out.println(response);
        String eventResponseData;
        try {
            eventResponseData = mapper.readValue(response, String.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return new ArrayList<>();
    }

}

