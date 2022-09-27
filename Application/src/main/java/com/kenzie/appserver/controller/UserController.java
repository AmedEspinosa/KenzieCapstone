package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
        UserResponse userResponse = userService.getUserById(id);
        if (userResponse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody CreateUserRequest createUserRequest){
        if (createUserRequest == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Customer Name");
        }
        UserResponse userResponse = userService.createUser(createUserRequest);

        return ResponseEntity.created(URI.create("/user/" + userResponse.getName())).body(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {

        UserResponse userResponse = userService.updateUser(userUpdateRequest);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
