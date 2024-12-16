package com.api.helpdesk.controller;

import com.api.helpdesk.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.helpdesk.service.UserService;

import java.util.List;


@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO users) {
        UserDTO savedUser = userService.register(users);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<UserDTO> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
