package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public String ListTags(Model model) {
        List<Tag> tags = tagService.getAllTags();

        model.addAttribute("tags", tags);
        return "tag/list";
    }

    @GetMapping("/create")
    public String showCreateForm (Model model) {
        model.addAttribute("tag", new Tag());

        return "tag/form";
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.createTag(tag);
            redirectAttributes.addFlashAttribute("message", "Tag created Successfully!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/tags";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm (@PathVariable Long id, Model model) {
        Tag tag = tagService.getTagById(id);

        model.addAttribute("tag", tag);

        return "tag/form";
    }

    @PostMapping("/{id}/edit")
    public String updateTag (@PathVariable Long id, @ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.updateTag(id, tag);
            redirectAttributes.addFlashAttribute("message", "Tag updated successfully");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/tags";
    }

    @PostMapping("/{id}/delete")
    public String deleteTag (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("message", "Tag deleted successfully!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/tags";
    }
}
