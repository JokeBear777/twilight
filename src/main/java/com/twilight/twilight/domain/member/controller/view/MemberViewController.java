package com.twilight.twilight.domain.member.controller.view;

import com.twilight.twilight.domain.member.dto.MyPageMemberInfoDto;
import com.twilight.twilight.domain.member.dto.MyPageUpdateDto;
import com.twilight.twilight.domain.member.service.MemberService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberViewController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String myPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
            ) {
        MyPageMemberInfoDto dto = memberService.getMyPageMemberInfo(userDetails.getMember());
        model.addAttribute("memberInfo", dto);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String myPageEdit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
            ) {
        MyPageMemberInfoDto dto = memberService.getMyPageMemberInfo(userDetails.getMember());
        model.addAttribute("memberInfo", dto);
        return "mypage-edit";
    }

    @PostMapping("/mypage/edit")
    public String updateMemberInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute MyPageUpdateDto formData
            ) {
        memberService.updateMemberInfo(userDetails.getMember(), formData);

        return "redirect:/mypage";
    }

}
