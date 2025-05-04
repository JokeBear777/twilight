package com.twilight.twilight.domain.member.controller.api;

import com.twilight.twilight.domain.member.dto.AddMemberRequestDto;
import com.twilight.twilight.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute AddMemberRequestDto addMemberRequestDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "signup";
        }
        memberService.signup(addMemberRequestDto);
        return "redirect:/login";
    }

    

}
