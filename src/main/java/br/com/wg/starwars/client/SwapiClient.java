package br.com.wg.starwars.client;

import br.com.wg.starwars.model.document.Planet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class SwapiClient {
	
	private final WebClient swapiClient;

    public SwapiClient(WebClient.Builder builder) {
		this.swapiClient = builder.baseUrl("https://swapi.dev/api/").build();
    }

	public Mono<Planet> findById(String id) {
		log.info("Buscando o planeta de id: [{}]", id);
		return swapiClient
				.get()
				.uri("planets/" + id)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Planet.class);
	}
}
