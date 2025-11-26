package shop.devictoria.api.google;

public record GoogleLoginResponse(
    boolean success,
    String message,
    String accessToken,
    String refreshToken,
    Object userData
) {
}

