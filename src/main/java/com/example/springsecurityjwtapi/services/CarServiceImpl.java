package com.example.springsecurityjwtapi.services;

import com.example.springsecurityjwtapi.entities.Car;
import com.example.springsecurityjwtapi.repositories.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService{

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> findAll() {
        log.info("Executing findAll Cars");
        return this.carRepository.findAll();
    }

    @Override
    public Optional<Car> findById(Long id) {
        log.info("Executing findById Cars");
        return this.carRepository.findById(id);
    }

    @Override
    public Car save(Car car) {
        log.info("Creating / Updating a Car");

        // Apply validations here

        return this.carRepository.save(car);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing a car by Id");
        if(!carRepository.existsById(id)){
            log.warn("Trying to delete an empty or null car list");
            return;
        }
        this.carRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        log.info("Removing all the cars");
        this.carRepository.deleteAll();
    }
}
