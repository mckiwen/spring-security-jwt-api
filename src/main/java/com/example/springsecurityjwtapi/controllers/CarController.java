package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.entities.Car;
import com.example.springsecurityjwtapi.services.CarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id){
        log.info("REST request to find a Car by an id given");
        Optional<Car> carOpt = this.carService.findById(id);
        return carOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> create(Car car){
        log.info("REST request to create a new Car");

        if(car.getId() != null){
            log.warn("Trying to create a new car with an existing id");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.carService.save(car));
    }

    @PutMapping("/cars")
    public ResponseEntity<Car> update(@RequestBody Car car){
        log.info("REST request to update an existing Car");
        if(car.getId() == null){
            log.warn("Trying to update a car without id");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.carService.save(car));
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Car> delete(@PathVariable Long id){
        log.info("REST request to delete an existing car");
        this.carService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cars")
    public ResponseEntity<Car> deleteAll(){
        log.info("REST request to delete all the cars");
        this.carService.deleteAll();
        return ResponseEntity.noContent().build();
    }


}
