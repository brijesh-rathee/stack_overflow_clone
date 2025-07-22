package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.*;
import com.stackclone.stackoverflow_clone.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final BookmarkService bookmarkService;

    private static final String USER_PROFILE_VIEW = "userprofile-page";

    @GetMapping({"/{userId}", "/{userId}/activity", "/{userId}/summary"})
    public String viewUserById(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);

        List<Question> allQuestions = user.getQuestions();
        List<Answer> allAnswers = user.getAnswers();
        Set<Tag>  allTags  = user.getFollowedTags();

        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("user", user);
        model.addAttribute("reputation", user.getReputation());
        model.addAttribute("badges", badgeService.getUserBadges(user));
        model.addAttribute("questions", allQuestions.stream().limit(5).toList());
        model.addAttribute("answers", allAnswers.stream().limit(5).toList());
        model.addAttribute("tags", allTags.stream().limit(5).toList());
        model.addAttribute("questionCount", allQuestions.size());
        model.addAttribute("answerCount",   allAnswers.size());
        model.addAttribute("activeTab", "activity");
        model.addAttribute("tab", "summary");

        return USER_PROFILE_VIEW;
    }
    @GetMapping("/{id}/profile")
    public String getProfilePage(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);


        model.addAttribute("user", user);
        model.addAttribute("activeTab", "profile");
        model.addAttribute("isCurrentUser", isCurrentUser);

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/bookmarks")
    public String viewUserBookmarks(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model) {
        User user = userService.getUserById(userId); // Get the user
        List<Bookmark> allBookMarks = userService.getAllBookMarks(userId); // Get bookmarks
        List<Question> savedQuestions = bookmarkService.getBookmarkedQuestion(user);

        model.addAttribute("user", user);
        model.addAttribute("bookmarks", allBookMarks);
        model.addAttribute("activeTab", "saves");
        model.addAttribute("savedQuestions",savedQuestions);

        return USER_PROFILE_VIEW;
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

    @GetMapping
    public String viewAllUsers(Model model) {
        List<User> aLlUsers = userService.getAllUsers();
        model.addAttribute("allUsers", aLlUsers);

        return "user-page";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam String query, Model model) {
        model.addAttribute("users", userService.searchBasic(query));
        return "fragments/user-list :: userList";
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

        return "redirect:/users/" + userId + "/profile";
    }

    @GetMapping("/{userId}/activity/questions")
    public String listUserQuestions(@PathVariable Long userId, Model model) {
        List<Question> questions = questionService.getQuestionsByUser(userId);
        User user = userService.getUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);

        model.addAttribute("user", user);
        model.addAttribute("questions", questions);
        model.addAttribute("tab", "questions");
        model.addAttribute("activeTab", "activity");

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/answers")
    public String listUserAnswers(@PathVariable Long userId, Model model) {
        List<Answer> answers = answerService.getAnswerByUserId(userId);
        User user = userService.getUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);

        model.addAttribute("user", user);
        model.addAttribute("answers", answers);
        model.addAttribute("tab", "answers");
        model.addAttribute("activeTab", "activity");
        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/following")
    public String findFollowers(@PathVariable Long userId, Model model){

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tab", "following");
        model.addAttribute("activeTab", "activity");


        return USER_PROFILE_VIEW;
    }
    @GetMapping("/{userId}/activity/responses")
    public String findResponses(@PathVariable Long userId, Model model){

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tab", "responses");
        model.addAttribute("activeTab", "activity");


        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/votes")
    public String viewUserVotes(@PathVariable Long userId, Model model){
        List<Vote> allVotes = userService.getAllVotesByUser(userId);
        User user = userService.getUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);

        model.addAttribute("user", user);
        model.addAttribute("tab", "votes");
        model.addAttribute("activeTab", "activity");
        model.addAttribute("votes",allVotes);

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/badges")
    public String getAllBadges(@PathVariable Long userId, Model model){
        Set<Badge> allBadges = userService.getAllBadgesByUser(userId);
        model.addAttribute("allBadges",allBadges);
        User user = userService.getUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        boolean isCurrentUser = user.getUsername().equals(loggedInUsername);

        model.addAttribute("user", user);
        model.addAttribute("allBadges", allBadges);
        model.addAttribute("tab", "badges");
        model.addAttribute("activeTab", "activity");

        return USER_PROFILE_VIEW;
    }
}
