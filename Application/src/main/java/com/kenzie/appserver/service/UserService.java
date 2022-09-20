package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(EventUserRepository eventUserRepository) {
        this.eventRepository = eventRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.eventUserRepository = eventUserRepository;
    }

    public UserResponse getUserById(String id){

        Optional<UserRecord> userRecord = eventUserRepository.findById(id);
        return userRecord.map(this::recordToResponse).orElse(null);
    }

    public UserResponse createUser(CreateUserRequest createUserRequest){

        UserRecord userRecord = new UserRecord();

        if (createUserRequest.getName() != null && createUserRequest.getId() != null && createUserRequest.getEmail() != null){
            userRecord.setId(createUserRequest.getId());
            userRecord.setName(createUserRequest.getName());
            userRecord.setEmail(createUserRequest.getEmail());
            eventUserRepository.save(userRecord);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User Request");
        }
        return recordToResponse(userRecord);
    }

    public void deleteUser(String userId){
        eventUserRepository.deleteById(userId);
    }

    public UserResponse updateUser(String id, String name, String email){
        Optional<UserRecord> userExists = eventUserRepository.findById(id);
        if (userExists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found");
        }
        UserRecord userRecord = userExists.get();
        userRecord.setName(name);
        userRecord.setEmail(email);
        eventUserRepository.save(userRecord);

        return recordToResponse(userRecord);
    }

    public UserResponse recordToResponse(UserRecord userRecord){

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userRecord.getId());
        userResponse.setName(userRecord.getName());
        userResponse.setEmail(userRecord.getEmail());

        return userResponse;
    }



}
