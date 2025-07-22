package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.TagService;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QuestionService questionService;
    private final UserService userService;
    private final TagService tagService;

    private static final String HOME_VIEW = "home-page";

    @GetMapping({"/", "/home"})
    public String showHomePage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {
        Page<Question> paginatedQuestions;

        if (keyword != null && !keyword.trim().isEmpty()) {
            paginatedQuestions = questionService.searchQuestions(keyword.trim().toLowerCase(), page, size);
            model.addAttribute("keyword", keyword);
        } else {
            paginatedQuestions = questionService.getPaginatedQuestions(page, size);
        }        List<Question> questions = paginatedQuestions.getContent();

        Map<Long, Integer> answerCounts = new HashMap<>();
        for (Question question : questions) {
            int count = question.getAnswers() != null ? question.getAnswers().size() : 0;
            answerCounts.put(question.getId(), count);
        }

        model.addAttribute("questions", questions);
        model.addAttribute("answerCounts", answerCounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedQuestions.getTotalPages());

        return HOME_VIEW;
    }


    @GetMapping("/questionslist")
    public String getQusestionsList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model){
        Page<Question> paginatedQuestions = questionService.getPaginatedQuestions(page, size);
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("questions", paginatedQuestions.getContent());
        model.addAttribute("tags",tags);
        model.addAttribute("totalQuestions",paginatedQuestions.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedQuestions.getTotalPages());

        return "questionslistpage";
    }

    @GetMapping("/companies")
    public String getCompanies(){
        return "company-page";
    }
}
