package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private EventUserRepository eventUserRepository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(EventUserRepository eventUserRepository) {
        this.eventUserRepository = eventUserRepository;
    }

    public UserResponse getUserById(String userId){

        Optional<UserRecord> userRecord = eventUserRepository.findById(userId);
        return userRecord.map(this::recordToResponse).orElse(null);
    }

    public UserResponse createUser(CreateUserRequest createUserRequest){

        UserRecord userRecord = new UserRecord();

        if (createUserRequest.getName() != null && createUserRequest.getEmail() != null){
            userRecord.setId(UUID.randomUUID().toString());
            userRecord.setName(createUserRequest.getName());
            userRecord.setEmail(createUserRequest.getEmail());
            eventUserRepository.save(userRecord);

            return recordToResponse(userRecord);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User Request, either name or email is absent");
        }
    }

    public void deleteUser(String userid){
        if (userid.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is Empty");
        }
        if (!eventUserRepository.existsById(userid)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id does not exist in the repository");
        }
        eventUserRepository.deleteById(userid);
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest){
        Optional<UserRecord> userExists = eventUserRepository.findById(userUpdateRequest.getId());
        if (userExists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found, does not exist in repository");
        }
        UserRecord userRecord = userExists.get();
        userRecord.setName(userUpdateRequest.getName());
        userRecord.setEmail(userUpdateRequest.getEmail());
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
