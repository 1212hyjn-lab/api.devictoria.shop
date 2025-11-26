package shop.devictoria.api.naver;

public record NaverLoginResponse(
    boolean success,
    String message,
    String accessToken,
    String refreshToken,
    Object userData
) {
}

