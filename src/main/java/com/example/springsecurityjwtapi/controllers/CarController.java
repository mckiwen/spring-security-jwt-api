package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.entities.Car;
import com.example.springsecurityjwtapi.services.CarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final Logger log = LoggerFactory.getLogger(CarController.class);

    private CarService carService;

    public CarController(CarService carService){
        this.carService = carService;
    }

    /* ============= SPRING CRUD METHODS ============ */

    @GetMapping("/cars")
    public List<Car> findAll(){
        log.info("REST request to find all cars");
        return this.carService.findAll();
    }





}
