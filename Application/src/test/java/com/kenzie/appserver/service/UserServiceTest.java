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
     *  exampleService.findById
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
    void deleteUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);
        when(eventUserRepository.existsById(userResponse.getId())).thenReturn(true);

        userService.deleteUser(userResponse.getId());
        verify(eventUserRepository).deleteById(userResponse.getId());
    }


}
