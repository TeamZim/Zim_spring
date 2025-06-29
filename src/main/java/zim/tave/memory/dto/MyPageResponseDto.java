package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    private UserInfo user;
    private Statistics statistics;
    private String flags;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String profileImageUrl;
        private String surName;
        private String firstName;
        private String koreanName;
        private String birth;
        private String nationality;
    }

    @Getter
    @AllArgsConstructor
    public static class Statistics {
        private Long countryCount;
        private Long diaryCount;
    }

}
