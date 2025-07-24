package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.service.Impl.AnswerServiceImpl;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import com.stackclone.stackoverflow_clone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerServiceImpl answerService;
    private final QuestionService questionService;
    private final UserService userService;
    private final VoteService voteService;

    @GetMapping("/user/{userId}")
    public String getAllAnswersByUser(@PathVariable Long userId, Model model){
        List<Answer> answerList = answerService.getAnswerByUserId(userId);
        model.addAttribute("allAnswer",answerList);

        return "";
    }
    @GetMapping("/edit/{id}")
    public String showEditAnswerForm(@PathVariable("id") Long id, Model model, Principal principal) {
        Answer answer = answerService.getAnswerById(id);

        Question question = answer.getQuestion();
        model.addAttribute("question", question);
        model.addAttribute("answers", question.getAnswers());
        model.addAttribute("newAnswer", answer);
        model.addAttribute("editMode", true);

        int questionScore = voteService.getQuestionScore(question);
        model.addAttribute("questionScore", questionScore);

        model.addAttribute("questionComments", question.getComments());

        Map<Long, Integer> answerScores = new HashMap<>();
        for (Answer ans : question.getAnswers()) {
            int score = voteService.getAnswerScore(ans);
            answerScores.put(ans.getId(), score);
        }
        model.addAttribute("answerScores", answerScores);

        Map<Long, List<?>> answerCommentsMap = new HashMap<>();
        for (Answer ans : question.getAnswers()) {
            answerCommentsMap.put(ans.getId(), ans.getComments());
        }
        model.addAttribute("answerCommentsMap", answerCommentsMap);

        return "question-view";
    }

    @PostMapping("/save/{questionId}")
    public String saveOrUpdateAnswer(@PathVariable Long questionId,
                                     @ModelAttribute("newAnswer") Answer answer,
                                     Principal principal) {
        if (answer.getId() != null) {
            Answer existing = answerService.getAnswerById(answer.getId());
            if (!existing.getUser().getUsername().equals(principal.getName())) {
                throw new SecurityException("Unauthorized");
            }
            existing.setContent(answer.getContent());
            answerService.updateAnswer(existing, existing.getId());
        } else {
            Question question = questionService.getQuestionById(questionId);
            answer.setQuestion(question);
            answer.setUser(userService.getLoggedInUser());
            answerService.createAnswer(answer);
        }

        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/{answerId}")
    public String getAnswerById(@PathVariable Long answerId,Model model){
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("answer",answer);

        return "";
    }
    @PostMapping("/{answerId}/delete")
    public String deleteAnswer(@PathVariable Long answerId) {
        Answer answer = answerService.getAnswerById(answerId);
        Question question = answer.getQuestion();
        Long questionId = question.getId();
        Answer acceptedAnswer = question.getAcceptedAnswer();

        if (acceptedAnswer == null || !acceptedAnswer.getId().equals(answerId)) {
            answerService.deleteAnswer(answerId);
        }

        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/{answerId}/upvote")
    public String upvoteAnswer(@PathVariable Long answerId) {
        voteService.voteAnswer(answerId, VoteType.UP);
        Long questionId = answerService.getAnswerById(answerId).getQuestion().getId();
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/{answerId}/downvote")
    public String downvoteAnswer(@PathVariable Long answerId) {
        voteService.voteAnswer(answerId, VoteType.DOWN);
        Long questionId = answerService.getAnswerById(answerId).getQuestion().getId();
        return "redirect:/questions/" + questionId;
    }

}