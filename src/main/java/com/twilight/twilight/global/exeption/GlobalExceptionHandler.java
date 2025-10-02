package com.twilight.twilight.global.exeption;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//예외가 다른 핸들러에서 안잡혔을때, 마지막 안전장치
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception e, Model model, HttpServletRequest request ) {
        String errorMessage = e.getMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }



}
