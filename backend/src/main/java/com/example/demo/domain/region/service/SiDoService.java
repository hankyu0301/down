package com.example.demo.domain.region.service;

import com.example.demo.domain.region.dto.jpql.RegionNameDTO;
import com.example.demo.domain.region.entity.SiDo;
import com.example.demo.domain.region.entity.SiGunGu;
import com.example.demo.domain.region.repository.SiDoRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SiDoService {

    private final SiDoRepository siDoRepository;

    @Transactional(readOnly = true)
    public List<SiDo> listSiDo() {
        return siDoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SiGunGu> listSiGunGu(Short siDoId) {
        SiDo siDo = siDoRepository.findById(siDoId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SI_DO));
        return siDo.getSiGunGuList();
    }

    @Transactional(readOnly = true)
    public RegionNameDTO getAddressName(Long legalAddrCode) {
        return siDoRepository.findSiDoAndSiGunGuNameByLegalAddressCode(legalAddrCode);
    }
}
