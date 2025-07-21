package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.*;

import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final TagService tagService;
    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionViewService questionViewService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;
    private final VoteService voteService;

    private static final String HOME_VIEW = "home-page";
    private static final String QUESTION_VIEW = "question-page";
    private static final String QUESTION_INFO_VIEW = "question-view";
    private static final String REDIRECT_HOME_VIEW = "redirect:/";

    @GetMapping("/ask")
    public String showAskQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("tags", tagService.getAllTags());

        return QUESTION_VIEW;
    }

    @GetMapping("/{id}")
    public String getQuestion(@PathVariable Long id, Model model, Principal principal) {
        Question currentQuestion = questionService.getQuestionById(id);
        int questionScore = voteService.getQuestionScore(currentQuestion);
        boolean bookmarked = false;

        if (principal != null) {
            User user = userService.getLoggedInUser();
            questionViewService.recordView(user, currentQuestion);
            bookmarked = bookmarkService.isBookmarked(user, currentQuestion);
        }

        List<Answer> answers = answerService.getAllAnswersByQuestionId(id);
        List<Comment> comments = commentService.getCommentByQuestionId(id);

        Map<Long, Integer> answerScores = new HashMap<>();
        for (Answer answer : answers) {
            answerScores.put(answer.getId(), voteService.getAnswerScore(answer));
        }

        Map<Long, List<Comment>> answerCommentsMap = commentService.getCommentsGroupedByAnswerIds(new ArrayList<>(answerScores.keySet()));

        model.addAttribute("question", currentQuestion);
        model.addAttribute("answers",answers);
        model.addAttribute("questionScore", questionScore);
        model.addAttribute("answerScores", answerScores);
        model.addAttribute("questionComments",comments);
        model.addAttribute("answerCommentsMap", answerCommentsMap);
        model.addAttribute("newAnswer", new Answer());

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

        return QUESTION_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@ModelAttribute Question question,
                                 @PathVariable Long id,
                                 @RequestParam List<Long> tagIds) {
        questionService.updateQuestion(question, id, tagIds);
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

    @PostMapping("/upvote/{id}")
    public String upvoteQuestion(@PathVariable Long id) {
        voteService.voteQuestion(id, VoteType.UP);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/downvote/{id}")
    public String downvoteQuestion(@PathVariable Long id) {
        voteService.voteQuestion(id, VoteType.DOWN);
        return "redirect:/questions/" + id;
    }

}