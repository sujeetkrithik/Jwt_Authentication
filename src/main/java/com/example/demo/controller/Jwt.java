package com.example.demo.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Jwt {
    @RequestMapping("/welcome")
        public String Welcome(){
        return "Welcome to Divum";

        }
    }

