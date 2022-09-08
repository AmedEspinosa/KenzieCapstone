package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.ExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

    EventController(ExampleService exampleService) {
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



}
