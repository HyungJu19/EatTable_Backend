package com.lec.spring.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String area;
    private String zipCode;
    private Double lat;
    private Double lng;
    private String district;
}
