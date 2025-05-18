package com.twilight.twilight.domain.bulletin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bulletin")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BulletinController {

    @GetMapping()
    public String bulletin() {



        return "bulletin";
    }

}
