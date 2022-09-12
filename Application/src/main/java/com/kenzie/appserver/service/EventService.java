package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.model.EventRepository;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private EventRepository eventRepository;
    private LambdaServiceClient lambdaServiceClient;

    public EventService(EventRepository eventRepository, LambdaServiceClient lambdaServiceClient) {
        this.eventRepository = eventRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

//    public Example findById(String id) {
//
//        // Example getting data from the lambda
//        ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);
//
//        // Example getting data from the local repository
//        Example dataFromDynamo = exampleRepository
//                .findById(id)
//                .map(example -> new Example(example.getId(), example.getName()))
//                .orElse(null);
//
//        return dataFromDynamo;
//    }

//    public Example addNewExample(String name) {
//        // Example sending data to the lambda
//        ExampleData dataFromLambda = lambdaServiceClient.setExampleData(name);
//
//        // Example sending data to the local repository
//        ExampleRecord exampleRecord = new ExampleRecord();
//        exampleRecord.setId(dataFromLambda.getId());
//        exampleRecord.setName(dataFromLambda.getData());
//        exampleRepository.save(exampleRecord);
//
//        Example example = new Example(dataFromLambda.getId(), name);
//        return example;
//    }

    public EventResponse getEventById(String id){
        Optional<EventRecord> record = eventRepository.findById(id);
        return record.map(this::recordToResponse).orElse(null);
    }

    public EventResponse updateEventById(String id, String name, String date, String organizer, List<String> listOfUsers,
                                                                                  String address, String description){
        Optional<EventRecord> eventExists = eventRepository.findById(id);
        if (eventExists.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer Not Found");
        }
        EventRecord eventRecord = eventExists.get();
        eventRecord.setName(name);
        eventRecord.setDate(date);
        eventRecord.setOrganizer(organizer);
        eventRecord.setListOfUsersAttending(listOfUsers);
        eventRecord.setAddress(address);
        eventRecord.setDescription(description);
        eventRepository.save(eventRecord);

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
        eventResponse.setOrganizer(eventRecord.getOrganizer());
        eventResponse.setListOfUsersAttending(eventRecord.getListOfUsersAttending());
        eventResponse.setAddress(eventRecord.getAddress());
        eventResponse.setDescription(eventRecord.getDescription());

        return eventResponse;
    }

}
