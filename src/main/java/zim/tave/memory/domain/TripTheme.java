package zim.tave.memory.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TripTheme {

    @Id @GeneratedValue
    private Long id;

    private String themeName;

    @JsonManagedReference
    @OneToMany(mappedBy = "tripTheme", cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();

    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setTripTheme(this);
    }
}
