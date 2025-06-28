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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Lob
    @Column(nullable = false)
    private String content;

    private String detailedLocation;

    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotionId")
    private Emotion emotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weatherId")
    private Weather weather;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> diaryImages = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void addDiaryImage(DiaryImage image) {
        diaryImages.add(image);
        image.setDiary(this);
    }

    public static Diary createDiary(User user, Trip trip, Country country, String city, 
                                   LocalDateTime dateTime, String content) {
        Diary diary = new Diary();
        diary.setUser(user);
        diary.setTrip(trip);
        diary.setCountry(country);
        diary.setCity(city);
        diary.setDateTime(dateTime);
        diary.setContent(content);
        diary.setCreatedAt(LocalDateTime.now());
        return diary;
    }

    public void setOptionalFields(String detailedLocation, String audioUrl, 
                                 Emotion emotion, Weather weather) {
        this.detailedLocation = detailedLocation;
        this.audioUrl = audioUrl;
        this.emotion = emotion;
        this.weather = weather;
    }
}
