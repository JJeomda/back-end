package com.jjeomda.backend.service;

import com.jjeomda.backend.dto.*;
import com.jjeomda.backend.models.User;
import com.jjeomda.backend.models.UserIdeal;
import com.jjeomda.backend.repository.UserIdealRepository;
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
    private final UserIdealRepository userIdealRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserIdealRepository userIdealRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userIdealRepository = userIdealRepository;
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

    // USER 정보 입력 비즈니스 로직
    public void registerUserInfo(Long loginUserId, RegisterUserInfoDto registerUserInfoDto) throws Exception {
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 유저입니다."));

        // 기존에 정보가 등록되어있으면 올바르지 않은 경로로 post 요청시 기존 정보가 다시 덮어 씌어지는 것 방지
        if(user.getName() != null) {
            throw new IllegalArgumentException("기존에 정보가 등록되어 있습니다.");
        }

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

    // USER 정보 조회 비즈니스 로직
    // 처음에 이런식으로 설계를 했는데, userId 를 받을 필요가 없었음 .
    // @AuthenticationPrincipal 를 통해 id 값을 받아올 수 있기 때문 .
    //    public UserInfoDto getUserInfo(Long userId, Long loginUserId) throws Exception {
    //        User user = userRepository.findById(userId)
    //                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 유저입니다."));
    //
    //        // 로그인한 유저 검증
    //        if (!loginUserId.equals(userId)) {
    //            throw new Exception("로그인 정보가 일치하지 않습니다.");
    //        }
    //
    //        UserInfoDto userInfoDto = new UserInfoDto(user.getName(), user.getMatchingStatus());
    //        return userInfoDto;
    //
    //    }

    // USER 정보 조회 비즈니스 로직
    public UserInfoDto getUserInfo(Long loginUserId) throws Exception {
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 유저입니다."));

        UserInfoDto userInfoDto = new UserInfoDto(user.getName(), user.getMatchingStatus(), user.isStatus());
        return userInfoDto;

    }

    // USER 이상형 정보입력 비즈니스 로직
    public void registerUserIdealInfo(Long loginUserId, RegisterUserIdealInfoDto registerUserIdealInfoDto) throws Exception {
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 유저입니다."));

        // 기존에 정보가 등록되어있으면 올바르지 않은 경로로 post 요청시 기존 정보가 다시 덮어 씌어지는 것 방지
        if(user.getMatchingStatus() != -1) {
            throw new IllegalArgumentException("기존에 진행중인 매칭이 있습니다.");
        }


        UserIdeal userIdeal = new UserIdeal(
                registerUserIdealInfoDto.getAge(),
                registerUserIdealInfoDto.getResidence(),
                registerUserIdealInfoDto.getAlcohol(),
                registerUserIdealInfoDto.getTobacco(),
                registerUserIdealInfoDto.getTall(),
                registerUserIdealInfoDto.getHeight(),
                registerUserIdealInfoDto.getMbti(),
                registerUserIdealInfoDto.getJob(),
                registerUserIdealInfoDto.getHobby(),
                registerUserIdealInfoDto.getAppearance()
        );

        userIdealRepository.save(userIdeal);

        user.setUserIdeal(userIdeal); // 연관관계 맺기 !!!
        user.setMatchingStatus(0L);

        userRepository.save(user);

    }
}
