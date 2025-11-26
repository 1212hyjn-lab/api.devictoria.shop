package shop.devictoria.api.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/naver")
@Tag(name = "Naver Auth", description = "네이버 로그인 API")
public class NaverController {
    
    @Value("${naver.client-id}")
    private String clientId;
    
    @Value("${naver.redirect-uri}")
    private String redirectUri;
    
    private static final String NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    
    @GetMapping("/auth-url")
    @Operation(summary = "네이버 로그인 URL 생성", description = "네이버 로그인 화면으로 이동할 URL을 반환합니다")
    public ResponseEntity<NaverAuthUrlResponse> getAuthUrl() {
        System.out.println("=== 네이버 인증 URL 요청 ===");
        System.out.println("Client ID: " + clientId);
        System.out.println("Redirect URI: " + redirectUri);
        
        String authUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&state=STATE",
                NAVER_AUTH_URL, clientId, redirectUri);
        
        System.out.println("생성된 인증 URL: " + authUrl);
        System.out.println("네이버 인증 URL 승인 완료");
        
        return ResponseEntity.ok(new NaverAuthUrlResponse(authUrl));
    }
    
    @PostMapping("/login")
    @Operation(summary = "네이버 로그인", description = "네이버 인증 코드로 로그인합니다")
    public ResponseEntity<NaverLoginResponse> login(@RequestBody NaverLoginRequest request) {
        System.out.println("=== 네이버 로그인 요청 ===");
        System.out.println("인증 코드: " + request.code());
        System.out.println("리다이렉트 URI: " + request.redirectUri());
        System.out.println("요청 시간: " + System.currentTimeMillis());
        
        String accessToken = "naver-access-token-" + System.currentTimeMillis();
        String refreshToken = "naver-refresh-token-" + System.currentTimeMillis();
        
        System.out.println("생성된 Access Token: " + accessToken);
        System.out.println("생성된 Refresh Token: " + refreshToken);
        System.out.println("네이버 로그인 승인 완료");
        
        NaverLoginResponse response = new NaverLoginResponse(
                true,
                "네이버 로그인 성공",
                accessToken,
                refreshToken,
                request
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/callback")
    @Operation(summary = "네이버 로그인 콜백", description = "네이버 인증 후 리다이렉트되는 콜백 URL")
    public ResponseEntity<NaverLoginResponse> callback(@RequestParam(required = false) String code) {
        System.out.println("=== 네이버 콜백 요청 ===");
        System.out.println("인증 코드: " + code);
        System.out.println("리다이렉트 URI: " + redirectUri);
        System.out.println("콜백 시간: " + System.currentTimeMillis());
        
        // 무조건 승인
        String accessToken = "naver-access-token-" + System.currentTimeMillis();
        String refreshToken = "naver-refresh-token-" + System.currentTimeMillis();
        
        System.out.println("생성된 Access Token: " + accessToken);
        System.out.println("생성된 Refresh Token: " + refreshToken);
        System.out.println("네이버 로그인 승인 완료");
        
        NaverLoginRequest request = new NaverLoginRequest(code != null ? code : "mock-code", redirectUri);
        
        NaverLoginResponse response = new NaverLoginResponse(
                true,
                "네이버 로그인 성공",
                accessToken,
                refreshToken,
                request
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
