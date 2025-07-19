package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.service.Impl.QuestionServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionServiceImpl questionServiceImpl;

    private static final String HOME_VIEW = "home-page";
    private static final String QUESTION_VIEW = "question-page";

    @GetMapping("/ask")
    public String showAskQuestionForm() {
        return QUESTION_VIEW;
    }

    @GetMapping("/view/{id}")
    public String getQuestion(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionServiceImpl.getQuestionById(id));

        return "";
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Question> questions = questionServiceImpl.getAllQuestions();
        model.addAttribute("questions", questions);

        return HOME_VIEW;
    }

    @PostMapping("/submit")
    public String createQuestion(@ModelAttribute Question question) {
        questionServiceImpl.createQuestion(question);

        return "redirect:/questions/";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@ModelAttribute Question question, @PathVariable Long id) {
        questionServiceImpl.updateQuestion(question, id);

        return "redirect:/questions/";
    }

    @PostMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId) {
        questionServiceImpl.deleteQuestion(questionId);

        return "redirect:/questions/";
    }

    @GetMapping("/user/{userId}")
    public String getQuestionsByUserId(@PathVariable Long userId, Model model) {
        List<Question> userQuestions = questionServiceImpl.getQuestionsByUser(userId);
        model.addAttribute("questions", userQuestions);

        return HOME_VIEW;
    }
}

