package com.jjeomda.backend.service;

import com.jjeomda.backend.dto.SignupRequestDto;
import com.jjeomda.backend.models.User;
import com.jjeomda.backend.repository.UserRepository;
import com.jjeomda.backend.security.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입 비즈니스 로직
    public User registerUser(SignupRequestDto requestDto) {

        String email = requestDto.getEmail();

        // (1) 회원 ID 중복 확인
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }

        // (2) 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(email, password);
        user.setRoles(Collections.singletonList("ROLE_USER"));
        return userRepository.save(user);
    }

    // 로그인 비즈니스 로직
    public String loginUser(SignupRequestDto requestDto) {
        User member = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
