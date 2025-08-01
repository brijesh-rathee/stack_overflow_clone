package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.TagRepository;
import com.stackclone.stackoverflow_clone.service.TagService;

import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserService userService;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tagRepository.findByNameIgnoreCase(tag.getName()).isPresent()) {
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

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Tag> findByTagsByUserId(Long userId) {
        return tagRepository.findFollowedTagsByUserId(userId);
    }

    @Override
    public void followTag(String tagName, Long userId) {
        User user = userService.getUserById(userId);
        Tag tag = tagRepository.findByNameIgnoreCase(tagName).orElseThrow();

        user.getFollowedTags().add(tag);
        userService.registerUser(user);
    }

    @Override
    public void unfollowTag(String tagName, Long userId) {
        User user = userService.getUserById(userId);
        Tag tag = tagRepository.findByNameIgnoreCase(tagName).orElseThrow();

        user.getFollowedTags().remove(tag);
        userService.registerUser(user);
    }

    @Override
    public Set<String> getFollowedTagNames(Long userId) {
        User user = userService.getUserById(userId);
        Set<String> tagNames = new HashSet<>();

        Set<Tag> followedTags = user.getFollowedTags();

        for(Tag tag : followedTags) {
            tagNames.add(tag.getName());
        }

        return tagNames;
    }

    @Override
    public Page<Tag> getTagsFilteredAndSorted(String keyword, String sort, Pageable pageable) {
        if (sort == null || sort.isBlank()) {
            sort = "popular";
        }

        boolean isSearch = keyword != null && !keyword.isBlank();

        if (sort.equalsIgnoreCase("alphabetical")) {
            if (isSearch) {
                return tagRepository.searchByKeywordOrderByNameAsc(keyword, pageable);
            } else {
                return tagRepository.findAllByOrderByNameAsc(pageable);
            }
        } else {
            if (isSearch) {
                return tagRepository.searchByKeywordOrderByPopularity(keyword, pageable);
            } else {
                return tagRepository.findAllOrderByPopularity(pageable);
            }
        }
    }

    @Override
    public Page<Tag> getAllTagsPage(PageRequest pageRequest) {
        return tagRepository.findAll(pageRequest);
    }
}