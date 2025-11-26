package shop.devictoria.api.naver;

public record NaverTokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Integer expiresIn
) {
}

