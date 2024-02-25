package com.example.demo.domain.sports.repository;

import com.example.demo.domain.sports.entity.Sports;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SportsPagingAndSortingRepository extends PagingAndSortingRepository<Sports, Long> {
}
