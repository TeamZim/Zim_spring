package zim.tave.memory.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Trip {

    @Id @GeneratedValue
    @Column(name = "tripId")
    private Long id;

    @Column(length = 14, nullable = false)
    private String tripName;

    @Column(length = 56)
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isDeleted = false;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripThemeId")
    private TripTheme tripTheme;

    @Lob
    private String content;

    private String representativeImageUrl; // 여행의 대표 사진 URL

    @JsonManagedReference
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDate.now();
    }

    public void addDiary(Diary diary) {
        diaries.add(diary);
        diary.setTrip(this);
        this.endDate = diary.getCreatedAt().toLocalDate();
    }

    //==생성 메서드==//
    public static Trip createTrip(User user, String tripName, String description, TripTheme tripTheme) {
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setTripName(tripName);
        trip.setDescription(description);
        trip.setTripTheme(tripTheme);
        trip.setStartDate(LocalDate.now());
        return trip;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
