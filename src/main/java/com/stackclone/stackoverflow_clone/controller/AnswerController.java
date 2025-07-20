package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.service.Impl.AnswerServiceImpl;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerServiceImpl answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public String getAllAnswersByUser(@PathVariable Long userId, Model model){
        List<Answer> answerList = answerService.getAnswerByUserId(userId);
        model.addAttribute("allAnswer",answerList);

        return "";
    }
    @GetMapping("/edit/{answerid}")
    public String updateAnswerForm(@PathVariable Long answerId, Model model){
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("answerForm",answer);

        return "redirect:/questions/" + answer.getQuestion().getId();
    }
    @PostMapping("/edit/{id}")
    public String editAnswer(@PathVariable Long id,
                             @ModelAttribute Answer answer,
                             Principal principal) {
        answerService.updateAnswer(answer, id);

        return "redirect:/questions/" + answer.getQuestion().getId();
    }

    @PostMapping("/save/{questionId}")
    public String saveAnswer(@PathVariable Long questionId, @ModelAttribute("answer") Answer answer){
        Question question = questionService.getQuestionById(questionId);
        answer.setQuestion(question);
        answer.setUser(userService.getLoggedInUser());
        answerService.createAnswer(answer);

        return "redirect:/questions/" + questionId;
    }
    @GetMapping("/{answerId}")
    public String getAnswerById(@PathVariable Long answerId,Model model){
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("answer",answer);

        return "";
    }
    @PostMapping("/{answerId}/delete")
    public String deleteAnswer(@PathVariable Long answerId){
        Long questionId = answerService.getAnswerById(answerId).getQuestion().getId();
        answerService.deleteAnswer(answerId);

        return "redirect:/questions/" + questionId;
    }
}