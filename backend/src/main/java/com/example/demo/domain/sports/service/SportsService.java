package com.example.demo.domain.sports.service;


import com.example.demo.domain.sports.dto.command.SportsCommand;
import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.sports.repository.SportsPagingAndSortingRepository;
import com.example.demo.domain.sports.repository.SportsRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@RequiredArgsConstructor
@Service
@Transactional
public class SportsService {

    private final SportsRepository sportsRepository;
    private final SportsPagingAndSortingRepository sportsPagingAndSortingRepository;

    public Sports registerSports(SportsCommand cmd) {
        Assert.notNull(cmd, "cmd must not be null");

        if (sportsRepository.existsByName(cmd.getName())) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_SPORTS_NAME);
        }

        return sportsRepository.save(cmd.toEntity());
    }

    public Page<Sports> sportsList(Integer page, Integer size) {
        Assert.notNull(page, "page must not be null");
        Assert.notNull(size, "size must not be null");

        return sportsPagingAndSortingRepository.findAll((PageRequest.of(page, size)));
    }

    public Sports findById(Long id) {
        Assert.notNull(id, "id must not be null");

        return sportsRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_SPORTS));
    }

    public Sports updateSports(Long id, SportsCommand cmd) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(cmd, "cmd must not be null");

        Sports sports = sportsRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_SPORTS));

        if (sportsRepository.existsByName(cmd.getName())) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_SPORTS_NAME);
        }

        sports.setName(cmd.getName());
        return sportsRepository.save(sports);
    }

    public void deleteById(Long id) {
        Assert.notNull(id, "id must not be null");
        sportsRepository.deleteById(id);
    }
}