package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.Normalizer;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        // Validate password length
        if (password.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters");
            return "redirect:/register";
        }

        // Check if email already exists
        if (memberRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        // Generate username from firstName and lastName
        String username = generateUsername(firstName, lastName);

        // Create member
        Member member = Member.builder()
                .fname(firstName)
                .lname(lastName)
                .username(username)
                .email(email)
                .phone(phone)
                .password(password)
                .build();

        try {
            // Register with default role "DOCTORANT"
            authService.register(member, "DOCTORANT");
            redirectAttributes.addFlashAttribute("success", "Registration successful! You can now login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Generate a unique username from first and last name
     * Format: firstname.lastname (lowercase, no accents)
     * If exists, append a number
     */
    private String generateUsername(String firstName, String lastName) {
        // Normalize and clean names (remove accents, lowercase)
        String cleanFirst = normalizeString(firstName);
        String cleanLast = normalizeString(lastName);

        String baseUsername = cleanFirst + "." + cleanLast;

        // Check if username exists, if so append number
        String username = baseUsername;
        int counter = 1;
        while (memberRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    /**
     * Normalize string: remove accents, lowercase, remove special characters
     */
    private String normalizeString(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
        return normalized;
    }
}
