package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TripTheme {

    @Id @GeneratedValue
    private Long id;

    private String themeName;
}
