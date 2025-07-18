package com.stackclone.stackoverflow_clone.controller.rest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class QuestionRestController {

    @PostMapping("/questions")
    public ResponseEntity<?> addQuestions(){
        return null;
    }

}
