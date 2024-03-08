
package com.example.demo.domain.gethering.repository;

import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Gethering;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GetheringPagingAndSortingRepository extends PagingAndSortingRepository<Gethering, Long> {
}
