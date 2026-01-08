package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Role;
import com.example.labomasi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/add";
    }

    @PostMapping("/add")
    public String addRole(@RequestParam("roleName") String roleName, Model model) {
        if (roleService.existsByRolename(roleName)) {
            model.addAttribute("error", "Role already exists!");
            return "roles/add";
        }

        Role role = Role.builder()
                .rolename(roleName)
                .build();

        roleService.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return "redirect:/roles";
    }
}