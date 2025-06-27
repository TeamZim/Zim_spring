package zim.tave.memory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Country {

    @Id
    private String countryCode; // 예: "KR", "US"

    private String countryName;

    private String emoji;
}
