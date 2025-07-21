//package com.stackclone.stackoverflow_clone.service.impl;
//
//import com.stackclone.stackoverflow_clone.entity.Tag;
//import com.stackclone.stackoverflow_clone.repository.TagRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class TagSeederService {
//
//    private final TagRepository tagRepo;
//    private final RestTemplate rest = new RestTemplate();
//
//    private static final int PAGE_SIZE = 100;
//    private static final int MAX_TAGS = 1000;
//
//    private static final String TAG_API =
//            "https://api.stackexchange.com/2.3/tags?page=1&pagesize=100&order=desc&sort=popular&site=stackoverflow";
//
//
//    public void fetchAndSaveTags() {
//        int page = 1;
//        int totalSaved = 0;
//
//        while (totalSaved < MAX_TAGS) {
//            String url = String.format(TAG_API, page, PAGE_SIZE);
//            Map<String, Object> response = safeGet(url);
//            if (response == null) break;
//
//            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
//            if (items == null || items.isEmpty()) break;
//
//            List<Tag> batch = new ArrayList<>();
//            for (Map<String, Object> item : items) {
//                String tagName = (String) item.get("name");
//
//                if (tagRepo.existsByName(tagName)) continue;
//
//                // Get description from collectives
//                String description = "";
//                List<Map<String, Object>> collectives = (List<Map<String, Object>>) item.get("collectives");
//                if (collectives != null && !collectives.isEmpty()) {
//                    description = (String) collectives.get(0).getOrDefault("description", "");
//                }
//
//                batch.add(Tag.builder()
//                        .name(tagName)
//                        .description(description)
//                        .build());
//
//                totalSaved++;
//                if (totalSaved >= MAX_TAGS) break;
//            }
//
//            tagRepo.saveAll(batch);
//            System.out.println("✅ Saved " + totalSaved + " tags so far...");
//
//            if (!(Boolean) response.getOrDefault("has_more", false)) break;
//            backoff(response);
//            page++;
//        }
//
//        System.out.println("🎉 Finished saving tags: total = " + totalSaved);
//    }
//
//    private Map<String, Object> safeGet(String url) {
//        System.out.println("working on tags please wait");
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
//            System.out.println("⏳ API backoff " + secs + " s");
//            try { Thread.sleep(secs * 1_000L); }
//            catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
//        }
//    }
//}
