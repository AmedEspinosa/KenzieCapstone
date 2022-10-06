package com.kenzie.appserver.controller;//package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserService userService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private UserQueryUtility userQueryUtility;

    @BeforeAll
    public void setup(){ userQueryUtility = new UserQueryUtility(mvc);}

    @Test
    public void getUserById_validId_isSuccessful() throws Exception {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().first().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);

        // WHEN
        userQueryUtility.userControllerClient.getUserById(userResponse.getId())
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("name")
                        .value(is(userResponse.getName())))
                .andExpect(jsonPath("email")
                        .value(is(userResponse.getEmail())))
                .andExpect(status().isOk());

        userQueryUtility.userControllerClient.deleteUser(userResponse.getId());
    }

    @Test
    void getUserById_userResponse_isNull_throws_exception() throws Exception{

        String id = UUID.randomUUID().toString();
        userQueryUtility.userControllerClient.getUserById(id)
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUser_validRequest_isSuccessful() throws Exception {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().first().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);

        // WHEN
        userQueryUtility.userControllerClient.addNewUser(createUserRequest)
                .andExpect(jsonPath("name")
                        .value(is(createUserRequest.getName())))
                .andExpect(jsonPath("email")
                        .value(is(createUserRequest.getEmail())))
                .andExpect(status().is2xxSuccessful());

        userQueryUtility.userControllerClient.deleteUser(userResponse.getId());
    }

    @Test
    public void addNewUser_userRequestDoesNotExist() throws Exception {

        CreateUserRequest createUserRequest = null;
        userQueryUtility.userControllerClient.addNewUser(createUserRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUser_validRequest_isSuccessful() throws Exception {

        // GIVEN
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().first().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(userResponse.getId());
        userUpdateRequest.setName(mockNeat.names().first().get());
        userUpdateRequest.setEmail(mockNeat.emails().get());

        userQueryUtility.userControllerClient.updateUser(userUpdateRequest)
//                .andExpect(jsonPath("id")
//                        .value(is(userUpdateRequest.getId())))
                .andExpect(jsonPath("name")
                        .value(is(userUpdateRequest.getName())))
                .andExpect(jsonPath("email")
                        .value(is(userUpdateRequest.getEmail())))
                .andExpect(status().isOk());

        userQueryUtility.userControllerClient.deleteUser(userUpdateRequest.getId());
    }

    @Test
    public void deleteUser_validId_isSuccessful() throws Exception {

        // GIVEN
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName(mockNeat.names().first().get());
        createUserRequest.setEmail(mockNeat.emails().get());

        UserResponse userResponse = userService.createUser(createUserRequest);

       userQueryUtility.userControllerClient.deleteUser(userResponse.getId())
                .andExpect(status().isOk());

        userQueryUtility.userControllerClient.getUserById(userResponse.getId())
                .andExpect(status().isNotFound());
    }


}