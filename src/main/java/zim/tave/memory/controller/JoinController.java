package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.JoinRequestDto;
import zim.tave.memory.dto.UserResponseDto;
import zim.tave.memory.service.JoinService;

@RestController
@RequestMapping("/api/join")
@RequiredArgsConstructor
@Tag(name = "Join API", description = "회원가입 API (카카오 로그인 후 사용자 정보 입력)")
public class JoinController {
    private final JoinService joinService;

    @Operation(summary = "회원가입용 정보 입력",
            description = "카카오 로그인 후 사용자 정보 입력하여 계정 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "이미 가입된 계정, 입력값 오류 등"),
            @ApiResponse(responseCode = "500", description = "서버 오류")})
    @PostMapping
    public ResponseEntity<UserResponseDto> join(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 용 요청 데이터. 사용자 아이디 및 카카오 정보(아이디, 프로필 이미지), 추가 정보 등",
                    required = true,
                    content = @Content(schema = @Schema(implementation = JoinRequestDto.class))
            )
            @RequestBody JoinRequestDto requestDto) {
        User user = joinService.join(requestDto);
        UserResponseDto response = new UserResponseDto(
                //사용자 아이디
                user.getId(),

                //카카오 정보
                user.getKakaoId(),
                user.getProfileImageUrl(),

                //추가 정보 입력
                user.getSurName(),
                user.getFirstName(),
                user.getKoreanName(),
                user.getBirth(),
                user.getNationality(),

                //Statistics 정보
                user.getDiaryCount(),
                user.getVisitedCountryCount(),
                user.getFlags()
        );
        return ResponseEntity.ok(response);
    }
}
