package zim.tave.memory.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class TripTheme {

    @Id @GeneratedValue
    private Long id;

    private String themeName;
    
    private String sampleImageUrl;  // 사용자가 테마를 선택할 때 보여줄 샘플 이미지
    
    private String cardImageUrl;    // 실제 카드에 들어갈 이미지

    @JsonManagedReference
    @OneToMany(mappedBy = "tripTheme", cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();

    public TripTheme(String themeName, String sampleImageUrl, String cardImageUrl) {
        this.themeName = themeName;
        this.sampleImageUrl = sampleImageUrl;
        this.cardImageUrl = cardImageUrl;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setTripTheme(this);
    }
}
