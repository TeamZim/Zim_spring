package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Diary {

    @Id @GeneratedValue
    @Column(name = "diaryId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripId")
    private Trip trip;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "weatherId")
    private Weather weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "themeId")
    private TripTheme tripTheme;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "emotionId")
    private Emotion emotion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> diaryImages = new ArrayList<>();

    public void addDiaryImage(DiaryImage image) {
        diaryImages.add(image);
        image.setDiary(this);
    }

    @Lob
    private String content;

    private String audioUrl;
    private String city;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //==생성 메서드==//
    public static Diary createDiary(User user, Country country, String content, String city, Emotion emotion, DiaryImage... diaryImages) {
        Diary diary = new Diary();
        diary.setUser(user);
        diary.setCountry(country);
        diary.setCreatedAt(LocalDateTime.now());
        diary.setContent(content);
        diary.setEmotion(emotion);
        diary.setCity(city);
        for (DiaryImage diaryImage : diaryImages) {
            diary.addDiaryImage(diaryImage);
        }
        return diary;
    }
}
