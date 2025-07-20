package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag createTag(Tag tag);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag getTagById(Long id);

    List<Tag> getTagsByIds(List<Long> tagIds);
}
