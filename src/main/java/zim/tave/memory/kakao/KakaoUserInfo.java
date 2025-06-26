package zim.tave.memory.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

//카카오 정보 중 실제 사용하는 정보
@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private String kakaoId;
    private String profileImageUrl;
}
