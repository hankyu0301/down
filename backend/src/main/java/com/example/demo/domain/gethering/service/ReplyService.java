package com.example.demo.domain.gethering.service;

import com.example.demo.domain.gethering.dto.command.ReplyCommand;
import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.entity.Reply;
import com.example.demo.domain.gethering.repository.CommentRepository;
import com.example.demo.domain.gethering.repository.GetheringRepository;
import com.example.demo.domain.gethering.repository.ReplyRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final GetheringRepository getheringRepository;
    private final CommentRepository commentRepository;

    public Reply register(Long getheringId, Long commentId, Long userId, ReplyCommand cmd) {
        Assert.notNull(getheringId, "모임 ID를 입력해주세요.");
        Assert.notNull(commentId, "댓글 ID를 입력해주세요.");
        Assert.notNull(userId, "사용자 ID를 입력해주세요.");
        Assert.notNull(cmd, "대댓글 정보를 입력해주세요.");

        if (!getheringRepository.existsById(getheringId)) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_GETHERING);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_COMMENT));

        Reply reply = cmd.toEntity();
        reply.setUser(user);
        reply.setParent(comment);

        return replyRepository.save(reply);
    }

    public Reply modify(Long getheringId, Long commentId, Long replyId, Long userId, ReplyCommand cmd) {
        Assert.notNull(getheringId, "모임 ID를 입력해주세요.");
        Assert.notNull(commentId, "댓글 ID를 입력해주세요.");
        Assert.notNull(replyId, "대댓글 ID를 입력해주세요.");
        Assert.notNull(userId, "사용자 ID를 입력해주세요.");
        Assert.notNull(cmd, "대댓글 정보를 입력해주세요.");

        if (!getheringRepository.existsById(getheringId)) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_GETHERING);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        if (commentRepository.existsById(commentId)) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_COMMENT);
        }

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_REPLY));

        if (!reply.getUser().equals(user)) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_USER);
        }

        reply.setContent(cmd.getContent());

        return replyRepository.save(reply);
    }

    public void delete(Long getheringId, Long commentId, Long replyId, Long userId) {
        Assert.notNull(getheringId, "모임 ID를 입력해주세요.");
        Assert.notNull(commentId, "댓글 ID를 입력해주세요.");
        Assert.notNull(replyId, "대댓글 ID를 입력해주세요.");
        Assert.notNull(userId, "사용자 ID를 입력해주세요.");

        if (!getheringRepository.existsById(getheringId)) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_GETHERING);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        if (commentRepository.existsById(commentId)) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_COMMENT);
        }

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_REPLY));

        if (!reply.getUser().equals(user)) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_USER);
        }

        replyRepository.delete(reply);
    }
}