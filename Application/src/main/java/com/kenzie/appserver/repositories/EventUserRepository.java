package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface EventUserRepository extends CrudRepository<UserRecord, String> {
}
