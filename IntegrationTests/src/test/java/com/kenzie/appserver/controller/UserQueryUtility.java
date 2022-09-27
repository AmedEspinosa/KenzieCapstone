package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.controller.model.EventUpdateRequest;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class UserQueryUtility {

    public UserControllerClient userControllerClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserQueryUtility(MockMvc mvc) {
        this.mvc = mvc;
        this.userControllerClient = new UserControllerClient();
    }

    public class UserControllerClient {
        public ResultActions getUserById(String id) throws Exception {
            return mvc.perform(get("/users/{id}", id)
                    .accept(MediaType.APPLICATION_JSON));
        }

        public ResultActions addNewUser(CreateUserRequest createUserRequest) throws Exception {
            return mvc.perform(post("/users/")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(createUserRequest)));
        }

        public ResultActions updateUser(UserUpdateRequest userUpdateRequest) throws Exception {
            return mvc.perform(put("/users/{id}", userUpdateRequest.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userUpdateRequest)));
        }

        public ResultActions deleteUser(String userId) throws Exception {
            return mvc.perform(delete("/users/{id}", userId)
                    .accept(MediaType.APPLICATION_JSON));
        }
    }

    }

