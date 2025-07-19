package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;

import com.stackclone.stackoverflow_clone.service.QuestionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    private static final String HOME_VIEW = "home-page";
    private static final String QUESTION_VIEW = "question-page";
    private static final String QUESTION_INFO_VIEW = "question-view";
    private static final String REDIRECT_HOME_VIEW = "redirect:/questions/";

    @GetMapping("/ask")
    public String showAskQuestionForm() {
        return QUESTION_VIEW;
    }

    @GetMapping("/{id}")
    public String getQuestion(@PathVariable Long id, Model model) {
        Question currentQuestion = questionService.getQuestionById(id);
        model.addAttribute("question", currentQuestion);

        return QUESTION_INFO_VIEW;
    }

    @PostMapping("/submit")
    public String createQuestion(@ModelAttribute Question question) {
        questionService.createQuestion(question);

        return REDIRECT_HOME_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@ModelAttribute Question question, @PathVariable Long id) {
        questionService.updateQuestion(question, id);

        return REDIRECT_HOME_VIEW;
    }

    @PostMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);

        return REDIRECT_HOME_VIEW;
    }

    @GetMapping("/user/{userId}")
    public String getQuestionsByUserId(@PathVariable Long userId, Model model) {
        List<Question> userQuestions = questionService.getQuestionsByUser(userId);
        model.addAttribute("questions", userQuestions);

        return HOME_VIEW;
    }
}

