package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final MemberService memberService;

    @GetMapping("")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("member", member);

        return "profile/index";
    }
}