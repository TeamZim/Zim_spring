package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    @Schema(description = "사용자 아이디(필수)", example = "1")
    private Long userId;

    @Schema(description = "카카오 사용자 ID(필수)", example = "4317757086")
    private String kakaoId;

    @Schema(description = "프로필 이미지 URL(필수)", example = "http://img1.kakaocdn.net/profile.jpeg")
    private String profileImageUrl;

    @Schema(description = "사용자 성(필수)", example = "KANG")
    private String surName;

    @Schema(description = "사용자 이름(필수)", example = "JIHYE")
    private String firstName;

    @Schema(description = "사용자 한글 이름(필수)", example = "강지혜")
    private String koreanName;

    @Schema(description = "생년월일[yyyy-MM-dd](필수)", example = "2000-01-01")
    private LocalDate birth;

    @Schema(description = "국가명(필수)", example = "REPUBLIC OF KOREA")
    private String nationality;

    @Schema(description = "사용자가 방문한 국가 수(초기값 0)", example = "1")
    private Long diaryCount;

    @Schema(description = "사용자가 작성한 일기 수(초기값 0)", example = "1")
    private Long visitedCountryCount;

    @Schema(description = "방문한 국가의 국기 이모지(초기값 null)", example = "🇰🇷🇫🇷🇯🇵")
    private String flags;
}