package shop.devictoria.api.naver;

public record NaverLoginRequest(
    String code,
    String redirectUri
) {
}

