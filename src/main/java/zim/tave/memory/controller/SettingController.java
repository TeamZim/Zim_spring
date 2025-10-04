package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.security.CustomUserDetails;
import zim.tave.memory.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
@Tag(name = "Setting-controller", description = "설정 조회(로그아웃, 회원 탈퇴)")
public class SettingController {

    private final SettingService settingService;

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류, 존재하지 않는 사용자 등", content = @Content())
    })
    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        settingService.logout(userId);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @Operation(summary = "회원탈퇴", description = "사용자 계정을 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류, 존재하지 않는 사용자 등", content = @Content())
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        settingService.deleteAccount(userId);
        return ResponseEntity.ok("회원탈퇴 완료");
    }
}
