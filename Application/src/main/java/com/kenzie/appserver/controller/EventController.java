package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("id") String id) {
        EventResponse eventResponse = eventService.getEventById(id);
        if (eventResponse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventResponse);
    }

    // What do we want to be available to be updated?
    @PostMapping("/{id}")
    public ResponseEntity<EventResponse> updateCustomer(@RequestBody EventUpdateRequest eventUpdateRequest) {
        EventResponse eventResponse = eventService.updateEventById(eventUpdateRequest.getId(),
                                                               eventUpdateRequest.getName(),
                                                               eventUpdateRequest.getDate(),
                                                               eventUpdateRequest.getOrganizer(),
                                     Collections.singletonList(eventUpdateRequest.getListOfUsersAttending()),
                                                               eventUpdateRequest.getAddress(),
                                                               eventUpdateRequest.getDescription());

        return ResponseEntity.ok(eventResponse);
    }


}
