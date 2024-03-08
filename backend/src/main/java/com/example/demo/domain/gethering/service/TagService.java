package com.example.demo.domain.gethering.service;

import com.example.demo.domain.gethering.dto.command.TagCommand;
import com.example.demo.domain.gethering.entity.Tag;
import com.example.demo.domain.gethering.repository.TagRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public Tag register(TagCommand cmd) {
        return tagRepository.save(cmd.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Tag> getTag(String name) {
        return tagRepository.findByNameContaining(name);
    }

    public Tag update(Long id, TagCommand cmd) {
        Assert.notNull(id, "태그 ID를 입력해주세요.");
        Assert.notNull(cmd, "태그 정보를 입력해주세요.");

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TAG));

        tag.setName(cmd.getName());

        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        Assert.notNull(id, "태그 ID를 입력해주세요.");
        tagRepository.deleteById(id);
    }
}