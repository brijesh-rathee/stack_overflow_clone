package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.*;
import com.stackclone.stackoverflow_clone.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private static final String USER_PROFILE_VIEW = "userprofile-page";

    private final UserService userService;
    private final BadgeService badgeService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final BookmarkService bookmarkService;
    private final TagService tagService;

    @GetMapping({"/{userId}", "/{userId}/activity", "/{userId}/summary"})
    public String viewUserById(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        String loggedInUsername = userService.getLoggedInUser().getUsername();
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
        String loggedInUsername = userService.getLoggedInUser().getUsername();
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
        User user = userService.getUserById(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> savedQuestionsPage = bookmarkService.getBookmarkedQuestions(user, pageable);

        Map<Long, Integer> answerCounts = new HashMap<>();
        for (Question question : savedQuestionsPage.getContent()) {
            int count = question.getAnswers() != null ? question.getAnswers().size() : 0;
            answerCounts.put(question.getId(), count);
        }

        model.addAttribute("hometab", "saves");
        model.addAttribute("user", user);
        model.addAttribute("bookmarks", userService.getAllBookMarks(userId)); // Optional
        model.addAttribute("activeTab", "saves");
        model.addAttribute("savedQuestions", savedQuestionsPage.getContent());
        model.addAttribute("totalPages", savedQuestionsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("answerCounts", answerCounts);

        return USER_PROFILE_VIEW;
    }

    @GetMapping
    public String viewAllUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "createdAt") String sortField,
                               @RequestParam(defaultValue = "desc") String sortDir,
                               @RequestParam(required = false) String keyword,
                               Model model) {

        Page<User> usersPage = userService.getAllPaginatedUsers(page, size, sortField, sortDir, keyword);

        model.addAttribute("hometab", "users");
        model.addAttribute("allUsers", usersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("query", keyword);

        return "user-page";
    }

    @PostMapping("/{userId}/update")
    public String saveUpdatedUser(@ModelAttribute("user") User user,
                                  @PathVariable Long userId,
                                  @RequestParam(value = "removeProfile", defaultValue = "false") boolean removeProfile,
                                  @RequestParam(value = "mediaFile", required = false) MultipartFile file) {
        userService.updateUser(user, userId,removeProfile, file);

        return "redirect:/users/" + userId + "/profile";
    }

    @GetMapping("/{userId}/activity/questions")
    public String listUserQuestions(@PathVariable Long userId, Model model) {
        List<Question> questions = questionService.getQuestionsByUser(userId);
        User user = userService.getUserById(userId);

        model.addAttribute("questionCount", questions.size());
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

        model.addAttribute("user", user);
        model.addAttribute("answers", answers);
        model.addAttribute("answerCount", answers.size());
        model.addAttribute("tab", "answers");
        model.addAttribute("activeTab", "activity");

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/following")
    public String findFollowers(Model model){

        User user = userService.getLoggedInUser();
        Set<Tag> userFollowedTags = user.getFollowedTags();

        model.addAttribute("user", user);
        model.addAttribute("tab", "following");
        model.addAttribute("tags",userFollowedTags);
        model.addAttribute("isFollowedTags", tagService.getFollowedTagNames(user.getId()));
        model.addAttribute("activeTab", "activity");

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/votes")
    public String viewUserVotes(@PathVariable Long userId, Model model){
        List<Vote> allVotes = userService.getAllVotesByUser(userId);
        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tab", "votes");
        model.addAttribute("activeTab", "activity");
        model.addAttribute("votes",allVotes);

        return USER_PROFILE_VIEW;
    }

    @GetMapping("/{userId}/activity/badges")
    public String getAllBadges(@PathVariable Long userId, Model model){
        Set<Badge> allBadges = userService.getAllBadgesByUser(userId);
        int totalBadge = !allBadges.isEmpty() ? allBadges.size() : 0;

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("totalBadge", totalBadge);
        model.addAttribute("badges", allBadges);
        model.addAttribute("tab", "badges");
        model.addAttribute("activeTab", "activity");

        return USER_PROFILE_VIEW;
    }
}
