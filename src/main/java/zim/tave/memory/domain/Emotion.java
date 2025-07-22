package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter @Setter
public class Emotion {

    @Id @GeneratedValue
    @Column(name = "emotion_id")
    private Long id;

    private String name;
    private String colorCode;

    public Emotion(String name, String colorCode) {
        this.name = name;
        this.colorCode = colorCode;
    }
}
