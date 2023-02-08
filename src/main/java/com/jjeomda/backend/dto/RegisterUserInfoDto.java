package com.jjeomda.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserInfoDto {
    private String name;
    private String birth;
    private String sex;
    private String residence;
    private String alcohol;
    private String tobacco;
    private String tall;
    private String height;
    private String mbti;
    private String job;
    private String hobby;
    private String appearance;
}
