package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.service.Impl.AnswerServiceImpl;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Service
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerServiceImpl answerService;
    private final QuestionService questionService;

    @GetMapping("/user/{userId}")
    public String getAllAnswersByUser(@PathVariable Long userId, Model model){
        List<Answer> answerList = answerService.getAnswerByUserId(userId);
        model.addAttribute("allAnswer",answerList);
        return "";
    }
    @GetMapping("/edit/{id}")
    public String updateAnswerForm(@PathVariable Long answerId, Model model){
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute(answer);
        return "";
    }
    @PostMapping("/save/{answerId}")
    public String saveUpdatedAnswer(@ModelAttribute("answer") Answer answer,@PathVariable Long answerId){
        answerService.updateAnswer(answer,answerId);
        return "";
    }

    @GetMapping("/new/{questionId}")
    public String createAnswerForm(@PathVariable Long questionId, Model model) {
        model.addAttribute("answer",new Answer());
        model.addAttribute("questionId",questionId);
        return "";
    }

    @PostMapping("/save/{questionId}")
    public String saveAnswer(@PathVariable Long questionId, @ModelAttribute("answer") Answer answer){
        Question question = questionService.getQuestionById(questionId);
        answer.setQuestion(question);
        answerService.createAnswer(answer);
        return "";
    }
    @GetMapping("/{id}")
    public String getAnswerById(@PathVariable Long answerId,Model model){
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("answer",answer);
        return "";
    }
}
