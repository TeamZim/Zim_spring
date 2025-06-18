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

//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private Member member;

    @ManyToOne
    @JoinColumn(name = "tripId")
    private Trip trip;

    @OneToOne
    @JoinColumn(name = "weatherId")
    private Weather weather;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private TripTheme tripTheme;

//    @ManyToOne
//    @JoinColumn(name = "countryId")
//    private Country country;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> diaryImages = new ArrayList<>();

    @Lob
    private String content;

    private String audioUrl;
    private String city;

    private LocalDate date;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
