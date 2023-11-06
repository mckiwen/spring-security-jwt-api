package com.example.springsecurityjwtapi.entities;

import jakarta.persistence.*;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("Primary Key")
    private Long id;

    @Column(name = "Facturer")
    private String manufacturer;
    private String model;
    private Double cc;
    private Integer doors;

    private Integer Year;
    private LocalDate releaseDate;
    private Boolean available;

}
