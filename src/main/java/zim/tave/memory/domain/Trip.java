package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Trip {

    @Id @GeneratedValue
    @Column(name = "tripId")
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isDeleted;

//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private Member member;

    @ManyToOne
    @JoinColumn(name = "tripThemeId")
    private TripTheme tripTheme;
}
