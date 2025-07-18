package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.service.Impl.QuestionServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionServiceImpl questionServiceImpl;

    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable Long id){

        return questionServiceImpl. getQuestionById(id);
    }

    @GetMapping
    public List<Question> getQuestions(){

        return questionServiceImpl.getAllQuestions();
    }

    @PostMapping
    public void createQuestion( @RequestBody Question question ){
        questionServiceImpl.createQuestion(question);
    }

    @PostMapping("/{id}")
    public void updateQuestion(@RequestBody Question question, @PathVariable Long id){
        questionServiceImpl.updateQuestion(question,id);
    }

    @PostMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable Long id){
        questionServiceImpl.deleteQuestion(id);
    }

    @GetMapping("/userId")
    public List<Question> getQuestionsByUserId(@PathVariable Long userId){

        return questionServiceImpl.getQuestionsByUser(userId);
    }
}
