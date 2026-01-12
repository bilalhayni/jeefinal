package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Department;
import com.example.labomasi.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/add";
    }

    @PostMapping("/add")
    public String addDepartment(@RequestParam("name") String name,
                                @RequestParam("code") String code,
                                @RequestParam(value = "description", required = false) String description,
                                Model model) {
        // Validate code is not empty
        if (code == null || code.trim().isEmpty()) {
            model.addAttribute("error", "Department code is required!");
            model.addAttribute("department", new Department());
            return "departments/add";
        }

        // Normalize code to uppercase
        String normalizedCode = code.trim().toUpperCase();

        if (departmentService.existsByName(name)) {
            model.addAttribute("error", "Department name already exists!");
            model.addAttribute("department", new Department());
            return "departments/add";
        }

        if (departmentService.existsByCode(normalizedCode)) {
            model.addAttribute("error", "Department code already exists!");
            model.addAttribute("department", new Department());
            return "departments/add";
        }

        Department department = Department.builder()
                .name(name)
                .code(normalizedCode)
                .description(description)
                .build();

        departmentService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        model.addAttribute("department", department);
        return "departments/edit";
    }

    @PostMapping("/edit/{id}")
    public String editDepartment(@PathVariable Long id,
                                 @RequestParam("name") String name,
                                 @RequestParam("code") String code,
                                 @RequestParam(value = "description", required = false) String description,
                                 Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Validate code is not empty
        if (code == null || code.trim().isEmpty()) {
            model.addAttribute("error", "Department code is required!");
            model.addAttribute("department", department);
            return "departments/edit";
        }

        // Normalize code to uppercase
        String normalizedCode = code.trim().toUpperCase();

        // Check if name already exists for another department
        if (!department.getName().equals(name) && departmentService.existsByName(name)) {
            model.addAttribute("error", "Department name already exists!");
            model.addAttribute("department", department);
            return "departments/edit";
        }

        // Check if code already exists for another department
        if (!department.getCode().equals(normalizedCode) && departmentService.existsByCode(normalizedCode)) {
            model.addAttribute("error", "Department code already exists!");
            model.addAttribute("department", department);
            return "departments/edit";
        }

        department.setName(name);
        department.setCode(normalizedCode);
        department.setDescription(description);
        departmentService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }
}
