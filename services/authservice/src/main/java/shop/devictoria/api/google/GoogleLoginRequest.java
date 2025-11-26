package shop.devictoria.api.google;

public record GoogleLoginRequest(
    String code,
    String redirectUri
) {
}

