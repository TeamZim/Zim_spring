package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//클라이언트 앱에 accessToken 요청
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String accessToken;
}