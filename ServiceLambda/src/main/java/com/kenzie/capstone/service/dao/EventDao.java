package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.ExampleRecord;
import jdk.jfr.Event;

import java.util.List;

public class EventDao {
    private DynamoDBMapper mapper;

    public EventDao(DynamoDBMapper mapper){this.mapper = mapper;}

    public List<EventRecord> getEventById(String id) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setId(id);

        DynamoDBQueryExpression<EventRecord> queryExpression = new DynamoDBQueryExpression<EventRecord>()
                .withHashKeyValues(eventRecord)
                .withConsistentRead(false);

        return mapper.query(EventRecord.class, queryExpression);
    }
}
