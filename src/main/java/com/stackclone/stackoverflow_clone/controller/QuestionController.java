package com.stackclone.stackoverflow_clone.controller;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.stackclone.stackoverflow_clone.entity.*;

import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.service.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private static final String HOME_VIEW = "home-page";
    private static final String QUESTION_VIEW = "question-page";
    private static final String QUESTION_INFO_VIEW = "question-view";
    private static final String REDIRECT_HOME_VIEW = "redirect:/";

    private final QuestionService questionService;
    private final TagService tagService;
    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionViewService questionViewService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;
    private final VoteService voteService;

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
        boolean isBookmarkedByUser = false;

        if (principal != null) {
            User user = userService.getLoggedInUser();
            questionViewService.recordView(user, currentQuestion);
            isBookmarkedByUser = bookmarkService.isBookmarked(user, currentQuestion);
        }

        User user = userService.getLoggedInUser();
        boolean isFollowed = questionService.isFollowedByUser(id, user.getId());
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
        model.addAttribute("isBookmarkedByUser", isBookmarkedByUser);
        model.addAttribute("answerCommentsMap", answerCommentsMap);
        model.addAttribute("newAnswer", new Answer());
        model.addAttribute("isFollowed", isFollowed);

        return QUESTION_INFO_VIEW;
    }

    @PostMapping("/submit")
    public String createQuestion(@ModelAttribute Question question,
                                 @RequestParam List<Long> tagIds ,
                                 @RequestParam(value = "mediaFile", required = false) MultipartFile file
                                 ) {
        questionService.createQuestion(question, tagIds,file);

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

    @PostMapping("/{questionId}/accept-answer/{answerId}")
    public String acceptAnswer(@PathVariable Long questionId, @PathVariable Long answerId, Principal principal) {
        Question question = questionService.getQuestionById(questionId);
        Answer answer = answerService.getAnswerById(answerId);

        if (!question.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/questions/" + questionId;
        }

        if (question.getAcceptedAnswer() != null && question.getAcceptedAnswer().getId().equals(answerId)) {
            question.setAcceptedAnswer(null);
        } else {
            question.setAcceptedAnswer(answer);
        }
        questionService.saveQuestion(question);

        return "redirect:/questions/" + questionId;
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

    @PostMapping("/{id}/follow")
    public String followQuestion(@PathVariable Long id) {
        User user = userService.getLoggedInUser();
        questionService.followQuestion(id, user.getId());

        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/unfollow")
    public String unFollowQuestion(@PathVariable Long id) {
        User user = userService.getLoggedInUser();
        questionService.unfollowQuestion(id, user.getId());

        return "redirect:/questions/" + id;
    }
}