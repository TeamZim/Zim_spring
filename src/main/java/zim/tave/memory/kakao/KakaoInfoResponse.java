package zim.tave.memory.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카카오에서 받아오는 정보 매핑용 DTO
@Getter
@NoArgsConstructor
public class KakaoInfoResponse {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private Profile profile;

        @Getter
        @NoArgsConstructor
        public static class Profile {
            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}