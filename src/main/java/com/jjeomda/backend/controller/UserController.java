package com.jjeomda.backend.controller;

import com.jjeomda.backend.dto.SignupRequestDto;
import com.jjeomda.backend.models.User;
import com.jjeomda.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 가입 요청 처리
    @PostMapping("/api/signup")
    public String registerUser(@RequestBody SignupRequestDto requestDto) {
        return userService.registerUser(requestDto).getEmail();
    }

    // 로그인
    @PostMapping("/api/login")
    public String loginUser(@RequestBody SignupRequestDto requestDto) {
        return userService.loginUser(requestDto);
    }
}
