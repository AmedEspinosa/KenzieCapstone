package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.EventResponseData;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private ExampleDao exampleDao;
    private EventDao eventDao;

//    @Inject
//    public LambdaService(ExampleDao exampleDao) {
//        this.exampleDao = exampleDao;
//    }

    @Inject
    public LambdaService(EventDao eventDao){
        this.eventDao = eventDao;
    }

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ExampleRecord record = exampleDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }


    public EventResponseData getEventById(String id){
        List<EventRecord> eventResponses = eventDao.getEventById(id);
        if(eventResponses.size() > 0){
            return new EventResponseData(eventResponses.get(0).getId(), eventResponses.get(0).getName(), eventResponses.get(0).getDate(), eventResponses.get(0).getUser(), eventResponses.get(0).getListOfAttending(), eventResponses.get(0).getAddress(), eventResponses.get(0).getDescription());
        }
        return null;
    }
}
