package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.*;
import com.stackclone.stackoverflow_clone.service.AnswerService;
import com.stackclone.stackoverflow_clone.service.BadgeService;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BadgeService badgeService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    private static final String USER_PROFILE_VIEW = "userprofile-page";

    @GetMapping("/{userId}")
    public String viewUserById(@PathVariable Long userId, Model model){
        User user = userService.getUserById(userId);
        long questionCount = user.getQuestions().size();
        long answerCount = user.getAnswers().size();
        List<Badge> badges = badgeService.getUserBadges(user);

        model.addAttribute("user",user);
        model.addAttribute("badges", badges);
        model.addAttribute("questionCount", questionCount);
        model.addAttribute("answerCount", answerCount);
        model.addAttribute("followedTags", user.getFollowedTags());


        return  USER_PROFILE_VIEW;
    }

    @GetMapping("/registerForm")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());

        return "";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);

        return "";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userId){
        userService.deleteUser(userId);

        return "";
    }

    @GetMapping("/all")
    public String viewAllUsers(Model model){
        List<User> aLlUsers = userService.getAllUsers();
        model.addAttribute("allUsers",aLlUsers);

        return "user-page";
    }

    @GetMapping("/edit/{userId}")
    public String updateForm(@PathVariable Long userId , Model model){
        User user = userService.getUserById(userId);
        model.addAttribute("existingUser",user);

        return "";
    }

    @PostMapping("/{userId}/update")
    public String saveUpdatedUser(@ModelAttribute("user")User user,@PathVariable Long userId){
        userService.updateUser(user,userId);
        return "";
    }

    @GetMapping("/{userId}/questions")
    public String listUserQuestions(@PathVariable Long userId, Model model) {
        List<Question> questions = questionService.getQuestionsByUser(userId);

        model.addAttribute("questions", questions);

        return "user/questions";
    }

    @GetMapping("/{userId}/answers")
    public String listUserAnswers(@PathVariable Long userId, Model model) {
        List<Answer> answers = answerService.getAnswerByUserId(userId);

        model.addAttribute("answers", answers);

        return "user/answers";
    }

    @GetMapping("/{userId}/bookmarks")
    public String viewUserBookmarks(@PathVariable Long userId, Model model){
        List<Bookmark> allBookMarks = userService.getAllBookMarks(userId);
        model.addAttribute("bookmarks",allBookMarks);
        return "";
    }

    @GetMapping("/{userId}/allVotes")
    public String viewUserVotes(@PathVariable Long userId, Model model){
        List<Vote> allVotes = userService.getAllVotesByUser(userId);
        model.addAttribute("votes",allVotes);

        return "";
    }

    @GetMapping("/{userid}/badges")
    public String getAllBadges(@PathVariable Long userId, Model model){
        Set<Badge> allBadges = userService.getAllBadgesByUser(userId);
        model.addAttribute("allBadges",allBadges);

        return "";
    }
}
