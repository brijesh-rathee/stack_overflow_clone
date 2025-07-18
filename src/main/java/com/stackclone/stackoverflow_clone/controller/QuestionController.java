package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.service.QuestionService;
import org.springframework.stereotype.Controller;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
}
