package com.example.springsecurityjwtapi.repositories;

import com.example.springsecurityjwtapi.entities.Car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
