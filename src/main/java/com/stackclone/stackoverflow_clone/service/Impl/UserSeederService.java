//package com.stackclone.stackoverflow_clone.service.Impl;
//
//import com.stackclone.stackoverflow_clone.entity.Question;
//import com.stackclone.stackoverflow_clone.entity.User;
//import com.stackclone.stackoverflow_clone.enums.UserRole;
//import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
//import com.stackclone.stackoverflow_clone.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class UserSeederService {
//
//    private final UserRepository userRepo;
//    private final QuestionRepository questionRepo;
//    private final PasswordEncoder encoder;
//    private final RestTemplate rest = new RestTemplate();
//
//    private static final int USER_PAGE_SIZE = 100;
//    private static final int QUESTION_PAGE_SIZE = 100;
//    private static final int MAX_USERS_TOTAL = 1_000;
//
//    private static final String USER_API =
//            "https://api.stackexchange.com/2.3/users?page=%d&pagesize=%d&order=desc&sort=reputation&site=stackoverflow";
//
//    private static final String USER_QUESTIONS_API =
//            "https://api.stackexchange.com/2.3/users/%d/questions?page=%d&pagesize=%d&order=desc&sort=activity"
//                    + "&site=stackoverflow&filter=withbody";
//
//    public void runOnce() {
//        int userPage = 1;
//        int userCount = 0;
//
//        while (userCount < MAX_USERS_TOTAL) {
//            String userUrl = String.format(USER_API, userPage, USER_PAGE_SIZE);
//            Map<String, Object> userResp = safeGet(userUrl);
//            if (userResp == null) break;
//
//            List<Map<String, Object>> users = (List<Map<String, Object>>) userResp.get("items");
//            if (users == null || users.isEmpty()) break;
//
//            for (Map<String, Object> u : users) {
//                if (userCount >= MAX_USERS_TOTAL) break;
//
//                User savedUser = persistUserIfAbsent(u);
//                fetchAndPersistQuestions(savedUser);
//                userCount++;
//            }
//
//            backoff(userResp);
//            if (!(Boolean) userResp.getOrDefault("has_more", false)) break;
//            userPage++;
//        }
//
//        System.out.println("✅ Seeder finished. Users stored total = " + userCount);
//    }
//
//    private User persistUserIfAbsent(Map<String, Object> u) {
//        Long soId = ((Number) u.get("user_id")).longValue();
//        String name = (String) u.get("display_name");
//
//        Optional<Object> already = userRepo.findByUsername(name);
//        if (already.isPresent()) return (User) already.get();
//
//        User entity = User.builder()
//                .username(name)
//                .email(name.toLowerCase().replaceAll("\\s+", "_") + "@example.com")
//                .password(encoder.encode("123"))
//                .reputation(((Number) u.getOrDefault("reputation", 0)).intValue())
//                .bio((String) u.getOrDefault("about_me", ""))
//                .location((String) u.getOrDefault("location", ""))
//                .website((String) u.getOrDefault("link", ""))
//                .role(UserRole.USER)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        return userRepo.save(entity);
//    }
//
//    private void fetchAndPersistQuestions(User owner) {
//        int page = 1;
//
//        while (true) {
//            String qUrl = String.format(USER_QUESTIONS_API,
//                    ((Number) owner.getId()).longValue(), page, QUESTION_PAGE_SIZE);
//
//            Map<String, Object> qResp = safeGet(qUrl);
//            if (qResp == null) break;
//
//            List<Map<String, Object>> items = (List<Map<String, Object>>) qResp.get("items");
//            if (items == null || items.isEmpty()) break;
//
//            List<Question> batch = new ArrayList<>();
//            for (Map<String, Object> q : items) {
//                Long soQid = ((Number) q.get("question_id")).longValue();
//                if (questionRepo.existsById(soQid)) continue;
//
//                batch.add(mapToQuestion(q, owner));
//            }
//
//            questionRepo.saveAll(batch);
//            backoff(qResp);
//            if (!(Boolean) qResp.getOrDefault("has_more", false)) break;
//            page++;
//        }
//    }
//
//    private Question mapToQuestion(Map<String, Object> q, User owner) {
//        Instant created = Instant.ofEpochSecond(((Number) q.get("creation_date")).longValue());
//        System.out.println("📌 Storing question: " + q.get("title"));
//        return Question.builder()
//                .questionId(((Number) q.get("question_id")).longValue())
//                .title((String) q.get("title"))
//                .content((String) q.getOrDefault("body", ""))
//                .scores(((Number) q.getOrDefault("score", 0)).intValue())
//                .viewCount(((Number) q.getOrDefault("view_count", 0)).intValue())
//                .link((String) q.getOrDefault("link", ""))
//                .user(owner)
//                .build();
//    }
//
//    private Map<String, Object> safeGet(String url) {
//        try {
//            return rest.getForObject(url, Map.class);
//        } catch (HttpClientErrorException e) {
//            System.err.println("❌ API error: " + e.getStatusCode() + " " + e.getMessage());
//            return null;
//        }
//    }
//
//    private void backoff(Map<String, Object> resp) {
//        if (resp.containsKey("backoff")) {
//            int secs = (int) resp.get("backoff");
//            System.out.println("⏳ API backoff " + secs + " s");
//            try {
//                Thread.sleep(secs * 1_000L);
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//}
