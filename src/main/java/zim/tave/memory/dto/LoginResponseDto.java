package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//로그인 후 응답
@Getter
@AllArgsConstructor
public class LoginResponseDto {

    // 기존 회원 여부 확인
    private boolean registered;

    private String kakaoId;
    private String profileImageUrl;
}
