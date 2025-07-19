package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QuestionService questionService;
    private final UserService userService;

    private static final String HOME_VIEW = "home-page";

    @GetMapping({"/","/home"})
    public String showHomePage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        Page<Question> paginatedQuestions = questionService.getPaginatedQuestions(page, size);
        model.addAttribute("questions", paginatedQuestions.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedQuestions.getTotalPages());

        return HOME_VIEW;
    }
}
