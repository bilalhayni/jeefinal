package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.model.entity.Role;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String listMembers(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "6") int size,
                              @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Member> memberPage = memberService.searchByLastName(keyword, PageRequest.of(page, size));

        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("pages", new int[memberPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "members/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("roles", roleService.findAll());
        return "members/add";
    }

    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member,
                            @RequestParam("confirmPassword") String confirmPassword,
                            @RequestParam("selectedRole") Long roleId,
                            Model model) {

        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        // Validation
        if (memberService.existsByUsername(member.getUsername())) {
            model.addAttribute("usernameError", "Username already exists!");
            return "members/add";
        }

        if (!member.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordError", "Passwords do not match!");
            return "members/add";
        }

        if (member.getUsername().contains(" ")) {
            model.addAttribute("usernameError", "Username must not contain spaces!");
            return "members/add";
        }

        // Add role
        Role role = roleService.findById(roleId).orElse(null);
        if (role != null) {
            member.getRoles().add(role);
        }

        // Set lastname to uppercase
        member.setLname(member.getLname().toUpperCase());

        memberService.save(member);
        return "redirect:/member";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id).orElse(null);
        if (member == null) {
            return "redirect:/member";
        }
        model.addAttribute("member", member);
        return "members/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member updatedMember) {
        Member member = memberService.findById(id).orElse(null);
        if (member == null) {
            return "redirect:/member";
        }

        member.setFname(updatedMember.getFname());
        member.setLname(updatedMember.getLname().toUpperCase());
        member.setEmail(updatedMember.getEmail());
        member.setPhone(updatedMember.getPhone());

        // Only update password if provided
        if (updatedMember.getPassword() != null && !updatedMember.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(updatedMember.getPassword()));
        }

        memberService.update(member);
        return "redirect:/member";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member";
    }

    @GetMapping("/addRole/{memberId}")
    public String showAddRoleForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return "redirect:/member";
        }
        model.addAttribute("member", member);
        model.addAttribute("availableRoles", roleService.findAll());
        return "members/addRoleToMember";
    }

    @PostMapping("/addRole")
    public String addRoleToMember(@RequestParam("memberId") Long memberId,
                                  @RequestParam("roleId") Long roleId,
                                  Model model) {

        Member member = memberService.findById(memberId).orElse(null);
        Role role = roleService.findById(roleId).orElse(null);

        if (member == null || role == null) {
            model.addAttribute("error", "Invalid member or role.");
            return "members/addRoleToMember";
        }

        if (member.getRoles().contains(role)) {
            model.addAttribute("member", member);
            model.addAttribute("availableRoles", roleService.findAll());
            model.addAttribute("error", "Member already has this role.");
            return "members/addRoleToMember";
        }

        memberService.addRoleToMember(memberId, roleId);
        return "redirect:/member";
    }

    @PostMapping("/{memberId}/removeRole/{roleId}")
    public String removeRoleFromMember(@PathVariable Long memberId, @PathVariable Long roleId) {
        memberService.removeRoleFromMember(memberId, roleId);
        return "redirect:/member";
    }
}