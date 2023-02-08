package com.jjeomda.backend.models;

import lombok.*;
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

    public User(User kakaoUser) {
        this.email = kakaoUser.getEmail();
        this.password = kakaoUser.getPassword();
        this.kakaoId = kakaoUser.getKakaoId();
        this.roles = Collections.singletonList("ROLE_USER");
    }
}