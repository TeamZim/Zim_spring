package zim.tave.memory.kakao;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//accessToken으로 카카오 정보 가져옴
@Component
public class KakaoApiClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoUserInfo getKakaoUserInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoInfoResponse.class
        );

        KakaoInfoResponse body = response.getBody();
        if (body == null || body.getId() == null) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }

        return new KakaoUserInfo(
                String.valueOf(body.getId()),
                body.getKakaoAccount().getProfile().getProfileImageUrl()
        );
    }
}
