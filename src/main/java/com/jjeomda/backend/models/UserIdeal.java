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
    private String age;

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

    public UserIdeal(
            String age,
            String residence,
            String alcohol,
            String tobacco,
            String tall,
            String height,
            String mbti,
            String job,
            String hobby,
            String appearance) {
        this.age = age;
        this.residence = residence;
        this.alcohol = alcohol;
        this.tall = tall;
        this.tobacco = tobacco;
        this.height = height;
        this.mbti = mbti;
        this.job = job;
        this.hobby = hobby;
        this.appearance = appearance;
    }
}


