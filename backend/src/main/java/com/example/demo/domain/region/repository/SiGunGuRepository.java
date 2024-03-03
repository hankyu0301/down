package com.example.demo.domain.region.repository;


import com.example.demo.domain.region.entity.SiGunGu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SiGunGuRepository extends JpaRepository<SiGunGu, Short> {
    boolean existsByLegalAddressCode(Long legalAddrCode);
}
