package com.jjeomda.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jjeomda.backend.dto.*;
import com.jjeomda.backend.models.User;
import com.jjeomda.backend.service.KakaoUserService;
import com.jjeomda.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public LoginDto loginUser(@RequestBody SignupRequestDto requestDto) {
        return userService.loginUser(requestDto);
    }

    // 카카오 로그인
    @PostMapping("/api/user/kakao/callback")
    public LoginDto kakoLogin(@RequestBody String code) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code);
    }

    // USER 정보 입력
    @PostMapping("/api/user/info/{userId}")
    public void registerUserInfo(@PathVariable(value = "userId") Long userId,
                                 @RequestBody RegisterUserInfoDto registerUserInfoDto,
                                 @AuthenticationPrincipal User user) throws Exception {
        userService.registerUserInfo(userId, registerUserInfoDto, user.getId());
    }

    // USER 정보 호출
    @GetMapping("/api/user/info/{userId}")
    public UserInfoDto getUserInfo (@PathVariable(value = "userId") Long userId,
                                    @AuthenticationPrincipal User user) throws Exception {
        return userService.getUserInfo(userId, user.getId());
    }

    // USER 이상형 정보 입력
    @PostMapping("/api/user/ideal/info/{userId}")
    public void registerUserIdealInfo(@PathVariable(value = "userId") Long userId,
                                 @RequestBody RegisterUserIdealInfoDto registerUserIdealInfoDto,
                                 @AuthenticationPrincipal User user) throws Exception {
        userService.registerUserIdealInfo(userId, registerUserIdealInfoDto, user.getId());
    }
}
