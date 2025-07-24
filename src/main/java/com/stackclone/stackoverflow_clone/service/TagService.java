package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<Tag> getAllTags();

    Tag createTag(Tag tag);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag getTagById(Long id);

    List<Tag> getTagsByIds(List<Long> tagIds);

    Tag findByName(String tagName);

    List<Tag> findByTagsByUserId(Long userId);

    Page<Tag> getPaginatedTags(Pageable pageable);

    void followTag(String tagName, Long userId);

    void unfollowTag(String tagName, Long userId);

    Set<String> getFollowedTagNames(Long userId);

    Page<Tag> getTagsFilteredAndSorted(String keyword, String sort, Pageable pageable);

    Page<Tag> getAllTagsPage(PageRequest of);
}
