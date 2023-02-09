package com.jjeomda.backend.models;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert // null 인 필드값이 insert 나 update 시 제외되게 .

// SpringSecurity 는 UserDetails 객체를 통해 권한 정보를 관리하기 때문에
// User 클래스에 UserDetails 를 구현하고 추가 정보를 재정의 해야 함 .
// Entity 와 UserDetails 은 구분할 수도 같은 클래스에서 관리할 수도 있음 .

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private Long kakaoId;

    @Column
    private String name;

    @Column
    private String birth;

    @Column
    private String sex;

    @Column
    private String residence;

    @Column
    private String alcohol;

    @Column
    private String tobacco;

    @Column
    private String tall;

    @Column
    private String height;

    @Column
    private String mbti;

    @Column
    private String job;

    @Column
    private String hobby;

    @Column
    private String appearance;

    // @Getter 가 boolean 타입에 대해서는 getXXX()이 아니라 isXXX() 의 형태로 getter 를 자동생성
    // 자기 정보를 입력했는지 true / false
    @Column(columnDefinition = "boolean default false")
    private boolean status;

    // 블라인드 매칭 진행 상태 ( -1, 0, 1 )
    @Column(columnDefinition = "Long default -1")
    private Long matchingStatus;

    // 유저 이상형 테이블 1:1 매핑
    @OneToOne
    private UserIdeal userIdeal;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 현재 로그인한 유저의 정보 받아오기 위함
    public Long getId() {
        return id;
    }

    // 일반 회원가입 생성자
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 카카오 회원가입 생성자
    public User(String email, String password, Long kakaoId) {
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
    }

    // 강제 로그인 생성자
    //    public User(User kakaoUser) {
    //        this.email = kakaoUser.getEmail();
    //        this.password = kakaoUser.getPassword();
    //        this.kakaoId = kakaoUser.getKakaoId();
    //        this.roles = Collections.singletonList("ROLE_USER");
    //    }
}