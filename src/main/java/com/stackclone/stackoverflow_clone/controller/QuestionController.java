package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.service.Impl.QuestionServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionServiceImpl questionServiceImpl;

    @GetMapping("/questions/{id}")
    public Question getQuestion(@PathVariable Long id){

        return questionServiceImpl. getQuestionById(id);
    }

    @GetMapping("/questions")
    public List<Question> getQuestions(){

        return questionServiceImpl.getAllQuestions();
    }

    @PostMapping("/questions")
    public void createQuestion( @RequestBody Question question ){
        questionServiceImpl.createQuestion(question);
    }

    @PutMapping("/questions/{id}")
    public void updateQuestion(@RequestBody Question question, @PathVariable Long id){
        questionServiceImpl.updateQuestion(question,id);
    }

    @DeleteMapping("/questions/{id}")
    public void deleteQuestion(@PathVariable Long id){
        questionServiceImpl.deleteQuestion(id);
    }
}
