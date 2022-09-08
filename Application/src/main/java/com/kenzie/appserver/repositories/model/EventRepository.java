package com.kenzie.appserver.repositories.model;

import com.kenzie.appserver.repositories.model.EventRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface EventRepository extends CrudRepository<EventRecord, String> {
}
