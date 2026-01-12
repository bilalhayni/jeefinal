package com.example.labomasi.controller;

import com.example.labomasi.exception.BadRequestException;
import com.example.labomasi.model.dto.form.MemberCreateForm;
import com.example.labomasi.model.dto.form.MemberEditForm;
import com.example.labomasi.model.entity.Department;
import com.example.labomasi.model.entity.Member;
import com.example.labomasi.service.DepartmentService;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final RoleService roleService;
    private final DepartmentService departmentService;

    /**
     * GET /members - List all members with pagination and search
     * DIRECTEUR can only see members from their own department
     */
    @GetMapping
    public String listMembers(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "5") int size,
                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                              Authentication authentication) {

        Page<Member> memberPage;

        // Check if user is DIRECTEUR - filter by their department
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DIRECTEUR"))) {
            // Get the current user's member record
            String email = authentication.getName();
            Member currentUser = memberService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            Department userDepartment = currentUser.getDepartment();

            if (userDepartment == null) {
                // DIRECTEUR without department sees no members
                memberPage = Page.empty();
            } else if (keyword != null && !keyword.isEmpty()) {
                // Search within department
                memberPage = memberService.searchByDepartmentAndLastName(userDepartment, keyword, PageRequest.of(page, size));
            } else {
                // Get all members from department
                memberPage = memberService.searchByDepartment(userDepartment, PageRequest.of(page, size));
            }

            model.addAttribute("userDepartment", userDepartment);
        } else {
            // ADMINISTRATEUR sees all members
            memberPage = memberService.searchByLastName(keyword, PageRequest.of(page, size));
        }

        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("pages", new int[memberPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "members/index";
    }

    /**
     * GET /members/new - Show member creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("memberForm", new MemberCreateForm());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "members/create";
    }

    /**
     * POST /members - Create new member
     */
    @PostMapping
    public String createMember(@Valid @ModelAttribute("memberForm") MemberCreateForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("departments", departmentService.findAll());
            return "members/create";
        }

        try {
            memberService.createMember(form);
            redirectAttributes.addFlashAttribute("success", "Member created successfully");
            return "redirect:/members";
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("departments", departmentService.findAll());
            return "members/create";
        }
    }

    /**
     * GET /members/{username} - Show member profile
     */
    @GetMapping("/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        Member member = memberService.getByUsername(username);
        model.addAttribute("member", member);
        return "members/profile";
    }

    /**
     * GET /members/{username}/edit - Show member edit form
     */
    @GetMapping("/{username}/edit")
    public String showEditForm(@PathVariable String username, Model model) {
        Member member = memberService.getByUsername(username);

        MemberEditForm form = MemberEditForm.builder()
                .firstName(member.getFname())
                .lastName(member.getLname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .roleId(member.getRole() != null ? member.getRole().getId() : null)
                .departmentId(member.getDepartment() != null ? member.getDepartment().getId() : null)
                .build();

        model.addAttribute("member", member);
        model.addAttribute("memberForm", form);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "members/edit";
    }

    /**
     * POST /members/{username} - Update member
     */
    @PostMapping("/{username}")
    public String updateMember(@PathVariable String username,
                               @Valid @ModelAttribute("memberForm") MemberEditForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Member member = memberService.getByUsername(username);
            model.addAttribute("member", member);
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("departments", departmentService.findAll());
            return "members/edit";
        }

        try {
            memberService.updateMember(username, form);
            redirectAttributes.addFlashAttribute("success", "Member updated successfully");
            return "redirect:/members";
        } catch (BadRequestException e) {
            Member member = memberService.getByUsername(username);
            model.addAttribute("member", member);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("departments", departmentService.findAll());
            return "members/edit";
        }
    }

    /**
     * POST /members/{username}/delete - Delete member
     */
    @PostMapping("/{username}/delete")
    public String deleteMember(@PathVariable String username, RedirectAttributes redirectAttributes) {
        memberService.deleteByUsername(username);
        redirectAttributes.addFlashAttribute("success", "Member deleted successfully");
        return "redirect:/members";
    }
}
