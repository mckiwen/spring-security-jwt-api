package com.example.springsecurityjwtapi.services;

import com.example.springsecurityjwtapi.entities.Car;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> findAll();

    Optional<Car> findById(Long id);

    Car save(Car car);

    void delete(Long id);

    void deleteAll();

}
