package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDto {

    @Schema(description = "카카오 사용자 ID(필수)", example = "4317757086")
    private String kakaoId;

    @Schema(description = "프로필 이미지 URL", example = "http://img1.kakaocdn.net/profile.jpeg")
    private String profileImageUrl;

    @Schema(description = "사용자 성", example = "KANG")
    private String surName;

    @Schema(description = "사용자 이름", example = "JIHYE")
    private String firstName;

    @Schema(description = "사용자 한글 이름", example = "강지혜")
    private String koreanName;

    @Schema(description = "생년월일 (yyyy-MM-dd)", example = "2000-01-01")
    private LocalDate birth;

    @Schema(description = "국가명", example = "REPUBLIC OF KOREA")
    private String nationality;

}
