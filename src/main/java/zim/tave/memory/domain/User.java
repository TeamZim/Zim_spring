package zim.tave.memory.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    //카카오 로그인
    @Column(unique=true)
    private String kakaoId;
    private String profileImageUrl;

    @Column(name = "sur_name")
    private String surName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "korean_name")
    private String koreanName;

    private LocalDate createdAt;
    private boolean status;
    private LocalDate birth;
    private String nationality;

    //마이페이지 Statistics 정보
    private Long diaryCount; //일기 수
    private Long visitedCountryCount; //방문한 나라 수

    @Column(length = 255)
    private String flags; //국기

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Setting setting;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();
}

