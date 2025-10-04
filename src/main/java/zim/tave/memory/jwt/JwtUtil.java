package zim.tave.memory.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import zim.tave.memory.domain.User;
import zim.tave.memory.repository.UserRepository;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 1000 * 60 * 60; // 1시간

    // 토큰 생성
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // userId 저장
                .claim("username", username)       // username claim 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    // userId 가져오기
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    // username 가져오기
    public String getUsernameFromToken(String token) {
        return parseClaims(token).get("username", String.class);
    }

    // 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Component
    @RequiredArgsConstructor
    public class TestTokenGenerator {

        private final JwtUtil jwtUtil;
        private final UserRepository userRepository;

        @EventListener(ApplicationReadyEvent.class)
        public void printTestUserToken() {
            User user = userRepository.findByKakaoId("test_강지혜")
                    .orElseThrow(() -> new RuntimeException("테스트 유저 없음"));
            String token = jwtUtil.generateToken(user.getId(), user.getKakaoId());
            System.out.println("🧩 테스트 유저 JWT 토큰: " + token);
        }
    }
}