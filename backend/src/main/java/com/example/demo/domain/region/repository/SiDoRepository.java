package com.example.demo.domain.region.repository;

import com.example.demo.domain.region.dto.jpql.RegionNameDTO;
import com.example.demo.domain.region.entity.SiDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SiDoRepository extends JpaRepository<SiDo, Short> {

    @Query("""
        SELECT new com.example.demo.domain.region.dto.jpql.RegionNameDTO(sd.name, sgg.name)
        FROM SiDo sd
        LEFT JOIN sd.siGunGuList sgg
        WHERE sgg.legalAddressCode = :legalAddressCode
    """)
    RegionNameDTO findSiDoAndSiGunGuNameByLegalAddressCode(Long legalAddressCode);
}
