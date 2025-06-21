package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Trip {

    @Id @GeneratedValue
    @Column(name = "tripId")
    private Long id;

    private String tripName;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isDeleted;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripThemeId")
    private TripTheme tripTheme;

    @Lob
    private String content;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    public void addDiary(Diary diary) {
        diaries.add(diary);
        diary.setTrip(this);
    }

//    //==생성 메서드==//
//    public static Trip createTrip(Member member, String tripName, LocalDate startDate, String content, TripTheme tripTheme) {
//        Trip trip = new Trip();
//        trip.setMember(member);
//        trip.setTripName(tripName);
//        trip.setStartDate(startDate);
//        trip.setContent(content);
//        trip.setTripTheme(tripTheme);
//        return trip;
//    }
}
