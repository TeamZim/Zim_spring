package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDto {

    private String kakaoId;
    private String profileImageUrl;
    private String surName;
    private String firstName;
    private String koreanName;
    private LocalDate birth;
    private String nationality;

}
