package com.jjeomda.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    private Long id;
    private String token;
    private boolean status;
}
