package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.TagService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping
    public String listTags(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size,
                           @RequestParam(name = "tagKeyword", required = false) String tagKeyword,
                           @RequestParam(name = "sort", required = false, defaultValue = "popular") String sort,
                           Model model, Principal principal) {

        Page<Tag> tagPage = tagService.getTagsFilteredAndSorted(tagKeyword, sort, PageRequest.of(page, size));

        model.addAttribute("hometab", "tags");
        model.addAttribute("tags", tagPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tagPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("tagKeyword", tagKeyword);
        model.addAttribute("sort", sort);

        if (principal != null) {
            User user = userService.getLoggedInUser();
            Set<String> followedTags = tagService.getFollowedTagNames(user.getId());
            model.addAttribute("followedTags", followedTags);
        }

        return "tags-page";
    }

    @GetMapping("/{userId}/activity")
    public String findAllTagsByUserId(@PathVariable Long userId, Model model){
        List<Tag> tags = tagService.findByTagsByUserId(userId);
        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tags", tags);
        model.addAttribute("tab", "tags");
        model.addAttribute("activeTab", "activity");

        return "userprofile-page";
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.createTag(tag);
            redirectAttributes.addFlashAttribute("message", "Tag created Successfully!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("/edit/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.updateTag(id, tag);
            redirectAttributes.addFlashAttribute("message", "Tag updated successfully");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("message", "Tag deleted successfully!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/admin/tags";
    }

    @GetMapping("/{tagName}")
    public String getQuestionsByTag(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @PathVariable String tagName,
                                    Model model) {
        Tag tag = tagService.findByName(tagName);

        Page<Question> paginatedQuestions = questionService.getPaginatedQuestionsByTag(tag, page, size);
        List<Question> questions = paginatedQuestions.getContent();

        Map<Long, Integer> answerCounts = new HashMap<>();
        for (Question question : questions) {
            int count = question.getAnswers() != null ? question.getAnswers().size() : 0;
            answerCounts.put(question.getId(), count);
        }

        model.addAttribute("tag", tag);
        model.addAttribute("questions", questions);
        model.addAttribute("answerCounts", answerCounts);
        model.addAttribute("totalQuestions", paginatedQuestions.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedQuestions.getTotalPages());

        return "tagQuestions";
    }

    @PostMapping("/follow")
    public String followTag(@RequestParam String tagName) {
        User user = userService.getLoggedInUser();
        Long userId = user.getId();
        tagService.followTag(tagName, userId);

        return "redirect:/tags";
    }

    @PostMapping("/unfollow")
    public String unfollowTag(@RequestParam String tagName) {
        User user = userService.getLoggedInUser();
        Long userId = user.getId();
        tagService.unfollowTag(tagName, userId);

        return "redirect:/tags";
    }

}
