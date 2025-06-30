package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zim.tave.memory.dto.LoginRequestDto;
import zim.tave.memory.dto.LoginResponseDto;
import zim.tave.memory.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@Tag(name = "Kakao Login API", description = "카카오 로그인 API")
public class LoginController {
    /*
    토큰 발급 URL
    https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8faa2ba724cbfef5c2abf54ebf9bed65&redirect_uri=http://localhost:8080/callback
    */
    private final LoginService loginService;

    @Operation(summary = "카카오 로그인 요청",
            description = "카카오 사용자 인증 후 토큰 발급, 토큰 이용하여 백엔드 서버가 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 accessToken 등)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "카카오 로그인 요청. 클라이언트 앱에 Access Token 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDto.class))
            )
            @RequestBody LoginRequestDto request) {
        LoginResponseDto response = loginService.login(request);
        return ResponseEntity.ok(response);
    }
}
