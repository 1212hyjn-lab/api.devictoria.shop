package shop.devictoria.api.dto;

public record KakaoLoginRequest(
    String code,
    String redirectUri
) {
}

