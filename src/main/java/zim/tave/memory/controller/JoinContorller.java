package zim.tave.memory.controller;

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
public class JoinContorller {
    private final JoinService joinService;

    @PostMapping
    public ResponseEntity<UserResponseDto> join(@RequestBody JoinRequestDto requestDto) {
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

                user.getDiaryCount(),
                user.getVisitedCountryCount(),
                user.getFlags()
        );
        return ResponseEntity.ok(response);
    }
}
