package shop.devictoria.api.kakao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoOAuthService {

    private final WebClient webClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    public KakaoOAuthService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * 카카오 액세스 토큰 발급
     */
    public KakaoTokenResponse getAccessToken(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "authorization_code");
            formData.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isEmpty()) {
                formData.add("client_secret", clientSecret);
            }
            formData.add("redirect_uri", redirectUri);
            formData.add("code", code);

            Map<String, Object> response = webClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("access_token")) {
                return new KakaoTokenResponse(
                        (String) response.get("access_token"),
                        (String) response.get("refresh_token"),
                        (String) response.get("token_type"),
                        (Integer) response.get("expires_in")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 발급 실패: " + e.getMessage(), e);
        }
        
        throw new RuntimeException("카카오 토큰 발급 실패");
    }

    /**
     * 카카오 사용자 정보 조회
     */
    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response;
        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패: " + e.getMessage(), e);
        }
    }
}

