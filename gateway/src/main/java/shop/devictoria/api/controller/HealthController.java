package shop.devictoria.api.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Configuration
@Tag(name = "Health", description = "Health check API")
public class HealthController {

	@Bean
	@Operation(summary = "Health check", description = "서버 상태를 확인합니다")
	public RouterFunction<ServerResponse> healthRoutes() {
		return route()
				.GET("/", request -> permanentRedirect(URI.create("/swagger-ui.html")).build())
				.GET("/api/health", request -> ok().bodyValue(Map.of(
						"status", "UP",
						"message", "API Gateway is running"
				)))
				.build();
	}
}

