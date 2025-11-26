package shop.devictoria.api.kakao;

public record KakaoLoginResponse(
    boolean success,
    String message,
    String accessToken,
    String refreshToken,
    Object userData
) {
}

