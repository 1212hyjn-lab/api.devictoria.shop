package shop.devictoria.api.kakao;

public record KakaoLoginRequest(
    String code,
    String redirectUri
) {
}

