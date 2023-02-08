package com.jjeomda.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jjeomda.backend.dto.SignupRequestDto;
import com.jjeomda.backend.service.KakaoUserService;
import com.jjeomda.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService,
                          KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
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

    // 카카오 로그인
    @PostMapping("/api/user/kakao/callback")
    // @RequestParam 은 생략 가능, 자동으로 인식해서 가져올 수 있음.
    public String kakoLogin(@RequestBody String code) throws JsonProcessingException {
        System.out.println(code);
        return kakaoUserService.kakaoLogin(code);
    }
}
