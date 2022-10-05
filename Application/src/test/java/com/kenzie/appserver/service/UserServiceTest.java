package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.EventUserRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private EventUserRepository eventUserRepository;
    private UserService userService;
    private final MockNeat mockNeat = MockNeat.threadLocal();


    @BeforeEach
    void setup() {
        eventUserRepository = mock(EventUserRepository.class);
        userService = new UserService(eventUserRepository);
    }
    /** ------------------------------------------------------------------------
     *  UserService.getUserById
     *  ------------------------------------------------------------------------ **/
    @Test
    void getUserById() {

        UserRecord userRecord = new UserRecord();
        userRecord.setId(UUID.randomUUID().toString());
        userRecord.setName(mockNeat.strings().get());
        userRecord.setEmail(mockNeat.strings().get());

        when(eventUserRepository.findById(userRecord.getId())).thenReturn(Optional.of(userRecord));

        UserResponse userResponse = userService.getUserById(userRecord.getId());

        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(userResponse.getId(), userRecord.getId(), "Id matches");
        Assertions.assertEquals(userResponse.getName(), userRecord.getName(), "Name matches");
        Assertions.assertEquals(userResponse.getEmail(), userRecord.getEmail(), "Email matches");
    }
    /** ------------------------------------------------------------------------
     *  UserService.createUser
     *  ------------------------------------------------------------------------ **/
    @Test
    void createUser() {

       CreateUserRequest createUserRequest = new CreateUserRequest();
       createUserRequest.setName(mockNeat.strings().get());
       createUserRequest.setEmail(UUID.randomUUID().toString());

       ArgumentCaptor<UserRecord> customerRecordCaptor = ArgumentCaptor.forClass(UserRecord.class);
       UserResponse userResponse = userService.createUser(createUserRequest);

       when(eventUserRepository.existsById(userResponse.getId())).thenReturn(true);

       verify(eventUserRepository).save(customerRecordCaptor.capture());
       Assertions.assertNotNull(userResponse);
       Assertions.assertEquals(userResponse.getId(), userResponse.getId(), "user id matches");
       Assertions.assertEquals(userResponse.getName(), createUserRequest.getName(), "user names matches");
       Assertions.assertEquals(userResponse.getEmail(), createUserRequest.getEmail(), "user email matches");
    }

    @Test
    void createUser_createEventRequest_nameIsNull_throws_exception() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(null);
        createUserRequest.setEmail(mockNeat.emails().get());
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserRequest));
        // THEN
        try {
            verify(eventUserRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the username is null. - " + error);
        }
    }

    @Test
    void createUser_createEventRequest_emailIsNull_throws_exception() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().get());
        createUserRequest.setEmail(null);
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserRequest));
        // THEN
        try {
            verify(eventUserRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the email is null. - " + error);
        }
    }

    /** ------------------------------------------------------------------------
     *  UserService.updateUser
     *  ------------------------------------------------------------------------ **/
    @Test
    void updateUser() {

        UserRecord userRecord = new UserRecord();
        userRecord.setId(UUID.randomUUID().toString());
        userRecord.setName(mockNeat.strings().get());
        userRecord.setEmail(UUID.randomUUID().toString());

        String newName = mockNeat.strings().get();
        String newEmail = UUID.randomUUID().toString();

        when(eventUserRepository.findById(userRecord.getId())).thenReturn(Optional.of(userRecord));
        ArgumentCaptor<UserRecord> userRecordArgumentCaptor = ArgumentCaptor.forClass(UserRecord.class);

        userService.updateUser(new UserUpdateRequest(userRecord.getId(), newName, newEmail));

        verify(eventUserRepository).save(userRecordArgumentCaptor.capture());
        UserRecord record = userRecordArgumentCaptor.getValue();

        Assertions.assertNotNull(record);
        Assertions.assertNotNull(record.getId(), "User id exists");
        Assertions.assertEquals(record.getName(), newName, "User name matches");
        Assertions.assertEquals(record.getEmail(), newEmail, "User email matches");
    }

    @Test
    void updateUser_userRecord_doesNotExist_throws_exception() {

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(randomUUID().toString());
        // WHEN
        when(eventUserRepository.findById(userUpdateRequest.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.updateUser(userUpdateRequest));
        // THEN
        try {
            verify(eventUserRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the username is null. - " + error);
        }
    }
    /** ------------------------------------------------------------------------
     *  UserService.deleteUser
     *  ------------------------------------------------------------------------ **/
    @Test
    void deleteUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);
        when(eventUserRepository.existsById(userResponse.getId())).thenReturn(true);

        userService.deleteUser(userResponse.getId());
        verify(eventUserRepository).deleteById(userResponse.getId());
    }

    @Test
    void deleteUser_userId_IsEmpty_throws_exception() {

        String eventId = "";
        when(eventUserRepository.findById(eventId)).thenReturn(Optional.empty());
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.deleteUser(eventId));
        // THEN
        try {
            verify(eventUserRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }

    @Test
    void deleteUser_userId_doesNotExist_throws_exception() {

        String eventId = randomUUID().toString();
        when(eventUserRepository.existsById(eventId)).thenReturn(false);
        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.deleteUser(eventId));
        // THEN
        try {
            verify(eventUserRepository, never()).save(Matchers.any());
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the event is not found in the database. - " + error);
        }
    }


}
