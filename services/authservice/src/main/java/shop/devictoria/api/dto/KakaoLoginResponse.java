package shop.devictoria.api.dto;

public record KakaoLoginResponse(
    boolean success,
    String message,
    String accessToken,
    String refreshToken,
    Object userData
) {
}

