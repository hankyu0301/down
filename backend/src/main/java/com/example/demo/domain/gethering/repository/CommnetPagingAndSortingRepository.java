package com.example.demo.domain.gethering.repository;

import com.example.demo.domain.gethering.entity.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommnetPagingAndSortingRepository extends PagingAndSortingRepository<Comment, Long> {
}
