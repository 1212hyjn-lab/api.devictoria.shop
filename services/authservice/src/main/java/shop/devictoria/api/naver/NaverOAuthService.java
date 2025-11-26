package shop.devictoria.api.naver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NaverOAuthService {

    private final WebClient webClient;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Value("${naver.token-uri}")
    private String tokenUri;

    @Value("${naver.user-info-uri}")
    private String userInfoUri;

    public NaverOAuthService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * 네이버 액세스 토큰 발급
     */
    public NaverTokenResponse getAccessToken(String code) {
        try {
            String url = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=STATE",
                    tokenUri, clientId, clientSecret, code);

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("access_token")) {
                return new NaverTokenResponse(
                        (String) response.get("access_token"),
                        (String) response.get("refresh_token"),
                        (String) response.get("token_type"),
                        (Integer) response.get("expires_in")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("네이버 토큰 발급 실패: " + e.getMessage(), e);
        }
        
        throw new RuntimeException("네이버 토큰 발급 실패");
    }

    /**
     * 네이버 사용자 정보 조회
     */
    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("response")) {
                return (Map<String, Object>) response.get("response");
            }
        } catch (Exception e) {
            throw new RuntimeException("네이버 사용자 정보 조회 실패: " + e.getMessage(), e);
        }
        
        throw new RuntimeException("네이버 사용자 정보 조회 실패");
    }
}

