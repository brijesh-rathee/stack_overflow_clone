package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.enums.UserRole;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final QuestionService questionService;
    private final TagService tagService;

    @GetMapping
    public String getAdminPage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userService.getAllUsersWithPagination(pageRequest);

        model.addAttribute("hometab", "admin");
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("activeTab", "users");
        model.addAttribute("role", null);

        return "admin-page";
    }

    @GetMapping("/users")
    public String viewUsers(@RequestParam(required = false) UserRole role,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {

        Page<User> usersPage = (role != null)
                ? userService.getUsersByRoleWithPagination(role, PageRequest.of(page, size))
                : userService.getAllUsersWithPagination(PageRequest.of(page, size));

        model.addAttribute("hometab", "admin");
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("role", role);
        model.addAttribute("activeTab", "users");

        return "admin-page";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) UserRole role) {

        userService.deleteUser(id);

        String redirectUrl = "/admin/users?page=" + page + "&size=" + size;
        if (role != null) {
            redirectUrl += "&role=" + role.name();
        }

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/questions")
    public String viewAllQuestions(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String filter,
                                   Model model) {
        Page<Question> questionPage;

        switch (filter != null ? filter : "") {
            case "lessVotes":
                questionPage = questionService.getQuestionsWithVoteLessThan(5, PageRequest.of(page, size));
                break;
            case "negativeVotes":
                questionPage = questionService.getQuestionsWithVoteLessThan(0, PageRequest.of(page, size));
                break;
            default:
                questionPage = questionService.getAllQuestionsPage(PageRequest.of(page, size));
        }

        model.addAttribute("hometab", "admin");
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("filter", filter);
        model.addAttribute("activeTab", "questions");
        return "admin-page";
    }

    @GetMapping("/tags")
    public String getTagsTab(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Page<Tag> tagPage = tagService.getAllTagsPage(PageRequest.of(page, size));

        model.addAttribute("hometab", "admin");
        model.addAttribute("tags", tagPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tagPage.getTotalPages());
        model.addAttribute("activeTab", "tags");

        return "admin-page";
    }

    @GetMapping("/tags/edit/{id}")
    public String showEditTagForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTagById(id);
        model.addAttribute("tag", tag);
        model.addAttribute("activeTab", "tags");
        model.addAttribute("isEdit", true);
        return "admin-page";
    }
    @PostMapping("/admin/tags/edit/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute("tag") Tag tag) {
        tag.setId(id);
        tagService.updateTag(id, tag);
        return "redirect:/admin/tags";
    }
}

