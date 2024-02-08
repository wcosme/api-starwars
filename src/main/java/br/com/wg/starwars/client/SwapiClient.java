package br.com.wg.starwars.client;

import br.com.wg.starwars.model.dto.PlanetDTO;
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

	public Mono<PlanetDTO> findById(String id) {
		log.info("Buscando o planeta de id: [{}]", id);
		return swapiClient
				.get()
				.uri("planets/" + id)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(PlanetDTO.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public Mono<PlanetDTO> findByName(String name) {
		log.info("Buscando o planeta de name: [{}]", name);
		return swapiClient
				.get()
				.uri("planets/" + name)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(PlanetDTO.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public <T> Mono<T> findByUrl(String url, Class<T> tipo) {
		log.info("Buscando o planeta de id: [{}]", url);
		return swapiClient
				.get()
				.uri(url)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(tipo)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}


}
