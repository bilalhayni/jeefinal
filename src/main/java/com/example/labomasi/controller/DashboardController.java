package com.example.labomasi.controller;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.model.entity.Project;
import com.example.labomasi.model.entity.Publication;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.service.DashboardService;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final PublicationService publicationService;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Current user
        Member member = memberService.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("member", member);

        // Stats
        model.addAttribute("memberCount", dashboardService.getMemberCount());
        model.addAttribute("projectCount", dashboardService.getProjectCount());
        model.addAttribute("pubCount", dashboardService.getPublicationCount());
        model.addAttribute("resourceCount", dashboardService.getResourceCount());

        // Active projects (no end date)
        List<Project> activeProjects = projectService.findActiveProjects();
        model.addAttribute("projectsWithoutEndDate", activeProjects);

        // Latest publications
        List<Publication> latestPublications = publicationService.findAll()
                .stream()
                .sorted((a, b) -> b.getPublicationDate().compareTo(a.getPublicationDate()))
                .limit(3)
                .toList();
        model.addAttribute("latestPublicationTitles", latestPublications.stream()
                .map(Publication::getTitle)
                .toList());

        // Member counts by role
        List<Object[]> memberCountsByRole = memberRepository.countMembersByRole();
        model.addAttribute("memberCountsByRole", memberCountsByRole);

        return "dashboard/index";
    }
}