package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zim.tave.memory.dto.MyPageResponseDto;
import zim.tave.memory.service.MyPageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    //마이페이지 조회
    @GetMapping("/{userId}")
    public ResponseEntity<MyPageResponseDto> getMyPage(@PathVariable Long userId) {
        MyPageResponseDto myPageResponseDto = myPageService.getMyPage(userId);
        return ResponseEntity.ok(myPageResponseDto);
    }
}
