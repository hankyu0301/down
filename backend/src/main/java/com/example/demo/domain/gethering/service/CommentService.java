package com.example.demo.domain.gethering.service;

import com.example.demo.domain.gethering.dto.command.CommentCommand;
import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.repository.CommentRepository;
import com.example.demo.domain.gethering.repository.CommnetPagingAndSortingRepository;
import com.example.demo.domain.gethering.repository.GetheringRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final GetheringRepository getheringRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommnetPagingAndSortingRepository commnetPagingAndSortingRepository;

    // 생성
    public Comment register(Long getheringId, Long userId, CommentCommand cmd) {
        Assert.notNull(getheringId, "getheringId must not be null");
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(cmd, "cmd must not be null");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        Gethering gethering = getheringRepository.findById(getheringId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_FOUND_GETHERING));

        Comment comment = cmd.toEntity();
        comment.setUser(user);
        comment.setGethering(gethering);

        return commentRepository.save(cmd.toEntity());
    }

    @Transactional(readOnly = true)
    public Page<Comment> list(Long getheringId, int page, int size) {
        Assert.notNull(getheringId, "getheringId must not be null");

        Gethering gethering = getheringRepository.findById(getheringId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_FOUND_GETHERING));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return commentRepository.findByGethering(gethering, PageRequest.of(page, size, sort));
    }

    public Comment update(Long getheringId, Long commentId, CommentCommand cmd) {
        Assert.notNull(getheringId, "getheringId must not be null");
        Assert.notNull(commentId, "commentId must not be null");
        Assert.notNull(cmd, "cmd must not be null");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_FOUND_COMMENT));

        comment.setContent(cmd.getContent());
        return commentRepository.save(comment);
    }

    public void delete(Long getheringId, Long commentId) {
        Assert.notNull(getheringId, "getheringId must not be null");
        Assert.notNull(commentId, "commentId must not be null");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_FOUND_COMMENT));

        commentRepository.delete(comment);
    }

    // 수정

    // 삭제
}