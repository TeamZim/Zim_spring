package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zim.tave.memory.dto.LoginResponseDto;
import zim.tave.memory.dto.MyPageResponseDto;
import zim.tave.memory.service.MyPageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "MyPage-Controller", description = "마이페이지 조회")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation (summary = "마이페이지 조회",
        description = "사용자 아이디 이용하여 마이페이지 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
                content = @Content(schema = @Schema(implementation = MyPageResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류, 존재하지 않는 사용자 조회", content = @Content())
    })
    @GetMapping("/{userId}")
    public ResponseEntity<MyPageResponseDto> getMyPage(
            @Parameter(description = "조회할 사용자 ID", example = "1")
            @PathVariable Long userId) {
        MyPageResponseDto myPageResponseDto = myPageService.getMyPage(userId);
        return ResponseEntity.ok(myPageResponseDto);
    }
}
