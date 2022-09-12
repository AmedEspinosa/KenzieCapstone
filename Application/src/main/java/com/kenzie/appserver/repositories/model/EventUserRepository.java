package com.kenzie.appserver.repositories.model;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface EventUserRepository extends CrudRepository<EventRecord, String> {
}
