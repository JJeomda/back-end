package com.jjeomda.backend.service;

import com.jjeomda.backend.dto.LoginDto;
import com.jjeomda.backend.dto.RegisterUserInfoDto;
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
        // "ROLE_USER" 를 default 권한으로 설정
        user.setRoles(Collections.singletonList("ROLE_USER"));
        return userRepository.save(user);
    }

    // 로그인 비즈니스 로직
    public LoginDto loginUser(SignupRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        LoginDto loginDto = new LoginDto(user.getId(), jwtTokenProvider.createToken(user.getUsername(), user.getRoles()), user.isStatus());
        return loginDto;
    }

    // 회원정보 입력 비즈니스 로직
    public void registerUserInfo(Long userId, RegisterUserInfoDto registerUserInfoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 유저입니다."));

        user.setName(registerUserInfoDto.getName());
        user.setBirth(registerUserInfoDto.getBirth());
        user.setSex(registerUserInfoDto.getSex());
        user.setResidence(registerUserInfoDto.getResidence());
        user.setAlcohol(registerUserInfoDto.getAlcohol());
        user.setTobacco(registerUserInfoDto.getTobacco());
        user.setTall(registerUserInfoDto.getTall());
        user.setHeight(registerUserInfoDto.getHeight());
        user.setMbti(registerUserInfoDto.getMbti());
        user.setJob(registerUserInfoDto.getJob());
        user.setHobby(registerUserInfoDto.getHobby());
        user.setAppearance(registerUserInfoDto.getAppearance());
        user.setStatus(true);

        userRepository.save(user);
    }
}
