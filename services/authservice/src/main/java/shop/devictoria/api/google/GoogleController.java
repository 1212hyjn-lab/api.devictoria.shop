package shop.devictoria.api.google;

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
@RequestMapping("/api/auth/google")
@Tag(name = "Google Auth", description = "구글 로그인 API")
public class GoogleController {
    
    @Value("${google.client-id}")
    private String clientId;
    
    @Value("${google.redirect-uri}")
    private String redirectUri;
    
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    
    @GetMapping("/auth-url")
    @Operation(summary = "구글 로그인 URL 생성", description = "구글 로그인 화면으로 이동할 URL을 반환합니다")
    public ResponseEntity<GoogleAuthUrlResponse> getAuthUrl() {
        System.out.println("=== 구글 인증 URL 요청 ===");
        System.out.println("Client ID: " + clientId);
        System.out.println("Redirect URI: " + redirectUri);
        
        String authUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&scope=openid%%20profile%%20email",
                GOOGLE_AUTH_URL, clientId, redirectUri);
        
        System.out.println("생성된 인증 URL: " + authUrl);
        System.out.println("구글 인증 URL 승인 완료");
        
        return ResponseEntity.ok(new GoogleAuthUrlResponse(authUrl));
    }
    
    @PostMapping("/login")
    @Operation(summary = "구글 로그인", description = "구글 인증 코드로 로그인합니다")
    public ResponseEntity<GoogleLoginResponse> login(@RequestBody GoogleLoginRequest request) {
        System.out.println("=== 구글 로그인 요청 ===");
        System.out.println("인증 코드: " + request.code());
        System.out.println("리다이렉트 URI: " + request.redirectUri());
        System.out.println("요청 시간: " + System.currentTimeMillis());
        
        String accessToken = "google-access-token-" + System.currentTimeMillis();
        String refreshToken = "google-refresh-token-" + System.currentTimeMillis();
        
        System.out.println("생성된 Access Token: " + accessToken);
        System.out.println("생성된 Refresh Token: " + refreshToken);
        System.out.println("구글 로그인 승인 완료");
        
        GoogleLoginResponse response = new GoogleLoginResponse(
                true,
                "구글 로그인 성공",
                accessToken,
                refreshToken,
                request
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/callback")
    @Operation(summary = "구글 로그인 콜백", description = "구글 인증 후 리다이렉트되는 콜백 URL")
    public ResponseEntity<GoogleLoginResponse> callback(@RequestParam(required = false) String code) {
        System.out.println("=== 구글 콜백 요청 ===");
        System.out.println("인증 코드: " + code);
        System.out.println("리다이렉트 URI: " + redirectUri);
        System.out.println("콜백 시간: " + System.currentTimeMillis());
        
        // 무조건 승인
        String accessToken = "google-access-token-" + System.currentTimeMillis();
        String refreshToken = "google-refresh-token-" + System.currentTimeMillis();
        
        System.out.println("생성된 Access Token: " + accessToken);
        System.out.println("생성된 Refresh Token: " + refreshToken);
        System.out.println("구글 로그인 승인 완료");
        
        GoogleLoginRequest request = new GoogleLoginRequest(code != null ? code : "mock-code", redirectUri);
        
        GoogleLoginResponse response = new GoogleLoginResponse(
                true,
                "구글 로그인 성공",
                accessToken,
                refreshToken,
                request
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
