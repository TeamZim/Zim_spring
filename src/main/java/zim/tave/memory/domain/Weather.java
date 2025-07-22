package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Weather {

    @Id @GeneratedValue
    @Column(name = "weather_id")
    private Long id;

    private String name;
    private String iconUrl;
}
