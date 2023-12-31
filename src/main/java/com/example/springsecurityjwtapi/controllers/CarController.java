package com.example.springsecurityjwtapi.controllers;

import com.example.springsecurityjwtapi.entities.Car;
import com.example.springsecurityjwtapi.services.CarService;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class CarController {

    private final Logger log = LoggerFactory.getLogger(CarController.class);

    private CarService carService;

    public CarController(CarService carService){
        this.carService = carService;
    }

    /* ============= SPRING CRUD METHODS ============ */

    /**
     * GET method that gives all the cars from the database.
     * @return List<Car>
     */
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> findAll() {
        log.info("REST request to find all cars");
        List<Car> listCar = this.carService.findAll();

        if (listCar.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(listCar);
        }
    }


    /**
     * GET method that gives a car for a requested id.
     * @param id
     * @return
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id){
        log.info("REST request to find a Car by an id given");
        Optional<Car> carOpt = this.carService.findById(id);
        return carOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST method to create a new Car.
     * @param car
     * @return
     */
    @PostMapping("/cars")
    public ResponseEntity<Car> create(@RequestBody Car car){
        log.info("REST request to create a new Car");

        if(car.getId() != null){
            log.warn("Trying to create a new car with an existing id");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.carService.save(car));
    }

    /**
     * PUT method to update an existing Car.
     * @param car
     * @return
     */
    @PutMapping("/cars")
    public ResponseEntity<Car> update(@RequestBody Car car){
        log.info("REST request to update an existing Car");
        if(car.getId() == null){
            log.warn("Trying to update a car without id");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.carService.save(car));
    }

    /**
     * DELETE method to delete an existing car for an id given.
     * @param id
     * @return
     */
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Car> delete(@PathVariable Long id){
        log.info("REST request to delete an existing car");
        this.carService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE method to delete all the cars from the database.
     * @return
     */
    @DeleteMapping("/cars")
    public ResponseEntity<Car> deleteAll(){
        log.info("REST request to delete all the cars");
        this.carService.deleteAll();
        return ResponseEntity.noContent().build();
    }


}
