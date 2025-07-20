package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;

import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.QuestionService;

import com.stackclone.stackoverflow_clone.service.QuestionViewService;
import com.stackclone.stackoverflow_clone.service.TagService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final TagService tagService;
    private final UserService userService;
    private final QuestionViewService questionViewService;

    private static final String HOME_VIEW = "home-page";
    private static final String QUESTION_VIEW = "question-page";
    private static final String QUESTION_INFO_VIEW = "question-view";
    private static final String REDIRECT_HOME_VIEW = "redirect:/";

    @GetMapping("/ask")
    public String showAskQuestionForm(Model model) {
        Question question = new Question();

        model.addAttribute("question", question);
        model.addAttribute("tags", tagService.getAllTags());

        return QUESTION_VIEW;
    }

    @GetMapping("/{id}")
    public String getQuestion(@PathVariable Long id, Model model, Principal principal) {
        Question currentQuestion = questionService.getQuestionById(id);

        if (principal != null) {
            User user = userService.getLoggedInUser();
            questionViewService.recordView(user, currentQuestion);
        }
        model.addAttribute("question", currentQuestion);

        return QUESTION_INFO_VIEW;
    }

    @PostMapping("/submit")
    public String createQuestion(@ModelAttribute Question question, @RequestParam List<Long> tagIds ) {
        User user = userService.getLoggedInUser();
        question.setUser(user);
        questionService.createQuestion(question, tagIds);

        return REDIRECT_HOME_VIEW;
    }

    @GetMapping("/update/{id}")
    public String showEditForm (@PathVariable Long id, Model model) {
        Question question = questionService.getQuestionById(id);
        List<Tag> tags = tagService.getAllTags();

        model.addAttribute("question", question);
        model.addAttribute("tags", tags);

        return "";
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

