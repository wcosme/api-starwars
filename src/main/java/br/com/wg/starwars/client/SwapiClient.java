package br.com.wg.starwars.client;

import br.com.wg.starwars.model.response.PlanetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class SwapiClient {
	
	private final WebClient swapiClient;

    public SwapiClient(WebClient.Builder builder) {
		this.swapiClient = builder.baseUrl("https://swapi.dev/api/").build();
    }

	public Mono<PlanetResponse> findById(String id) {
		log.info("Buscando o planeta de id: [{}]", id);
		return swapiClient
				.get()
				.uri("planets/" + id)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(PlanetResponse.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}
}
