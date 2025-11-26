package shop.devictoria.api.kakao;

public record KakaoTokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Integer expiresIn
) {
}

