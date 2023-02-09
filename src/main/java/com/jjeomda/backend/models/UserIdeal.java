package com.jjeomda.backend.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserIdeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // 유저 이상형 테이블 1:1 매핑
    @OneToOne(mappedBy = "userIdeal")
    private User user;
}
