package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Project;
import com.example.labomasi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("")
    public String listProjects(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Project> projectPage = projectService.searchByTitle(keyword, PageRequest.of(page, size));

        model.addAttribute("projects", projectPage.getContent());
        model.addAttribute("pages", new int[projectPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "projects/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/add";
    }

    @PostMapping("/add")
    public String addProject(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        return "projects/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute Project updatedProject) {
        Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return "redirect:/projects";
        }

        project.setTitle(updatedProject.getTitle());
        project.setDescription(updatedProject.getDescription());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());

        projectService.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/in-progress")
    public String activeProjects(Model model) {
        model.addAttribute("projectsWithoutEndDate", projectService.findActiveProjects());
        return "projects/en-cours";
    }
}