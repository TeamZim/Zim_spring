package zim.tave.memory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Country {

    @Id
    private String countryCode; // 예: "KR", "US"

    private String countryName;

    private String emoji;

    public Country(String countryCode, String countryName, String emoji) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.emoji = emoji;
    }

}
