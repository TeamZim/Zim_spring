package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//클라이언트 앱에 accessToken 요청
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "카카오 사용자 인증 후 토큰 발급, 토큰 이용하여 백엔드 서버가 로그인 처리",
    example = "v8C_EY5krpv_v8FGA3mCYO-BZoolYcBcAAAAAQoNG1kAAAGXsDd-Z6ew61y3DOUZ")
    private String accessToken;
}