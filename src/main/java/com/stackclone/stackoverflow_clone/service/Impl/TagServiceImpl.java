package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.repository.TagRepository;
import com.stackclone.stackoverflow_clone.service.TagService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new RuntimeException("Tag with this name already exist");
        }
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        existingTag.setName(tag.getName());
        existingTag.setDescription(tag.getDescription());

        return tagRepository.save(existingTag);
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not Found");
        }
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow();

        return tag;
    }

    @Override
    public List<Tag> getTagsByIds(List<Long> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }
}
