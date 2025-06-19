package zim.tave.memory.domain;

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

    @OneToMany(mappedBy = "tripTheme", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    public void addDiary(Diary diary) {
        diaries.add(diary);
        diary.setTripTheme(this);
    }
}
