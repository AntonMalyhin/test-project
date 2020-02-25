/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 */
@RestController()
@RequestMapping(path = "/api/test")
public class TestController {

    @Autowired
    public TestController() {
    }

    @GetMapping("")
    public ResponseEntity test() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
