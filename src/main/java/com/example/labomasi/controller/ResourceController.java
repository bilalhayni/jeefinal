package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Resource;
import com.example.labomasi.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("")
    public String listResources(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "6") int size,
                                @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Resource> resourcePage = resourceService.searchByName(keyword, PageRequest.of(page, size));

        model.addAttribute("resources", resourcePage.getContent());
        model.addAttribute("pages", new int[resourcePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "resources/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("resource", new Resource());
        return "resources/add";
    }

    @PostMapping("/add")
    public String addResource(@ModelAttribute Resource resource) {
        resourceService.save(resource);
        return "redirect:/resources";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Resource resource = resourceService.findById(id).orElse(null);
        if (resource == null) {
            return "redirect:/resources";
        }
        model.addAttribute("resource", resource);
        return "resources/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateResource(@PathVariable Long id, @ModelAttribute Resource updatedResource) {
        Resource resource = resourceService.findById(id).orElse(null);
        if (resource == null) {
            return "redirect:/resources";
        }

        resource.setName(updatedResource.getName());
        resource.setDescription(updatedResource.getDescription());
        resource.setAvailability(updatedResource.getAvailability());

        resourceService.save(resource);
        return "redirect:/resources";
    }

    @GetMapping("/delete/{id}")
    public String deleteResource(@PathVariable Long id) {
        resourceService.deleteById(id);
        return "redirect:/resources";
    }
}