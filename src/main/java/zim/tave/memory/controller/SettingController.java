package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {

    private final SettingService settingService;

    @PatchMapping("/{userId}/logout")
    public ResponseEntity logout(@PathVariable Long userId) {
        settingService.logout(userId);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable Long userId) {
        settingService.deleteAccount(userId);
        return ResponseEntity.ok("회원탈퇴 완료");
    }
}
