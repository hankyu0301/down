package com.example.demo.domain.region.dto.jpql;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionNameDTO {
    private String siDoName;
    private String siGunGuName;

    public String getRegion() {
        return siDoName + " " + siGunGuName;
    }
}