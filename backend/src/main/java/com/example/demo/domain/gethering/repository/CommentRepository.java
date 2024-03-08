package com.example.demo.domain.gethering.repository;

import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Gethering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByGethering(Gethering gethering, PageRequest pageRequest);
}
