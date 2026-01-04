package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Project;
import com.example.labomasi.model.entity.Publication;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pub")
public class PublicationController {

    private final PublicationService publicationService;
    private final ProjectService projectService;

    @GetMapping("")
    public String listPublications(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "6") int size,
                                   @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Publication> pubPage = publicationService.searchByTitle(keyword, PageRequest.of(page, size));

        model.addAttribute("pubs", pubPage.getContent());
        model.addAttribute("pages", new int[pubPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "publications/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("publication", new Publication());
        model.addAttribute("projects", projectService.findAll());
        return "publications/create";
    }

    @PostMapping("/add")
    public String addPublication(@ModelAttribute Publication publication,
                                 @RequestParam("project_id") Long projectId) {

        if (projectId != null) {
            Project project = projectService.findById(projectId).orElse(null);
            publication.setProject(project);
        }

        publicationService.save(publication);
        return "redirect:/pub";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Publication publication = publicationService.findById(id).orElse(null);
        if (publication == null) {
            return "redirect:/pub";
        }

        model.addAttribute("publication", publication);
        model.addAttribute("projects", projectService.findAll());
        return "publications/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePublication(@PathVariable Long id,
                                    @ModelAttribute Publication updatedPublication,
                                    @RequestParam("projectId") Long projectId) {

        Publication publication = publicationService.findById(id).orElse(null);
        if (publication == null) {
            return "redirect:/pub";
        }

        publication.setTitle(updatedPublication.getTitle());
        publication.setAuthor(updatedPublication.getAuthor());
        publication.setPublicationDate(updatedPublication.getPublicationDate());

        if (projectId != null) {
            Project project = projectService.findById(projectId).orElse(null);
            publication.setProject(project);
        }

        publicationService.save(publication);
        return "redirect:/pub";
    }

    @GetMapping("/delete/{id}")
    public String deletePublication(@PathVariable Long id) {
        publicationService.deleteById(id);
        return "redirect:/pub";
    }

    @GetMapping("/latest")
    public String latestPublications(Model model) {
        List<Publication> latest = publicationService.findAll()
                .stream()
                .sorted((a, b) -> b.getPublicationDate().compareTo(a.getPublicationDate()))
                .limit(3)
                .toList();

        model.addAttribute("latestPublicationTitles", latest.stream()
                .map(Publication::getTitle)
                .toList());

        return "publications/latest";
    }
}