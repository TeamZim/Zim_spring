package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    private UserInfo user;
    private Statistics statistics;

    @Schema(description = "방문한 국가의 국기 이모지", example = "🇰🇷🇫🇷🇯🇵")
    private String flags;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {

        @Schema(description = "사용자 아이디", example = "1")
        private Long userId;

        @Schema(description = "프로필 이미지 URL", example = "http://img1.kakaocdn.net/profile.jpeg")
        private String profileImageUrl;

        @Schema(description = "사용자 성", example = "KANG")
        private String surName;

        @Schema(description = "사용자 이름", example = "JIHYE")
        private String firstName;

        @Schema(description = "사용자 한글 이름", example = "강지혜")
        private String koreanName;

        @Schema(description = "사용자 생년월일", example = "22 2월/Feb 2002")
        private String birth;

        @Schema(description = "사용자 국적", example = "REPUBLIC OF KOREA")
        private String nationality;
    }

    @Getter
    @AllArgsConstructor
    public static class Statistics {

        @Schema(description = "사용자가 방문한 국가 수", example = "1")
        private Long countryCount;

        @Schema(description = "사용자가 작성한 일기 수", example = "1")
        private Long diaryCount;
    }

}
