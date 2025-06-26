package zim.tave.memory.controller;

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
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = loginService.login(request);
        return ResponseEntity.ok(response);
    }
}
