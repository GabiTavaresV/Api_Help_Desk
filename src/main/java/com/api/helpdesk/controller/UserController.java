package com.api.helpdesk.controller;

import com.api.helpdesk.entity.Users;
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
    public ResponseEntity<Users> createUser(@RequestBody Users users) {
        System.out.println("Dados recebidos para salvar: " + users);
        Users savedUser = userService.register(users);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<Users> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Users getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
