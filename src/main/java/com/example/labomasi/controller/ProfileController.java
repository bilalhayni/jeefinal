package com.example.labomasi.controller;

import com.example.labomasi.model.dto.form.ChangePasswordForm;
import com.example.labomasi.model.entity.Member;
import com.example.labomasi.service.AuthService;
import com.example.labomasi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("member", member);

        return "profile/index";
    }

    @GetMapping("/changepassword")
    public String showChangePasswordForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("member", member);
        model.addAttribute("changePasswordForm", new ChangePasswordForm());

        return "profile/change-password";
    }

    @PostMapping("/changepassword")
    public String changePassword(@Valid @ModelAttribute ChangePasswordForm form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // If validation errors exist
        if (bindingResult.hasErrors()) {
            model.addAttribute("member", member);
            return "profile/change-password";
        }

        // Check if new password and confirm password match
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("member", member);
            model.addAttribute("error", "New password and confirm password do not match");
            return "profile/change-password";
        }

        // Verify current password
        if (!authService.checkPassword(form.getCurrentPassword(), member.getPassword())) {
            model.addAttribute("member", member);
            model.addAttribute("error", "Current password is incorrect");
            return "profile/change-password";
        }

        // Change password
        try {
            authService.changePassword(member.getId(), form.getNewPassword());
            redirectAttributes.addFlashAttribute("success", "Password changed successfully");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("member", member);
            model.addAttribute("error", "Failed to change password: " + e.getMessage());
            return "profile/change-password";
        }
    }
}