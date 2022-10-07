package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
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

    public EventRecord postNewEvent(EventRecord record) {
        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return record;
    }


    public List<EventRecord> getAllEvents() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("attribute_not_exists(id)");

        return mapper.scan(EventRecord.class, scanExpression);
    }
}
