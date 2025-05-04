package com.twilight.twilight.global.viewController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BasicApiController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
