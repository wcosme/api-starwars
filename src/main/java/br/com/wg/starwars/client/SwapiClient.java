package br.com.wg.starwars.client;

import br.com.wg.starwars.model.document.Character;
import br.com.wg.starwars.model.document.Film;
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
		return swapiClient
				.get()
				.uri("planets/" + id)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(PlanetDTO.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public Mono<PlanetDTO> findByName(String name) {
		log.info("Buscando o planeta de name: [{}] findByName", name);
		return swapiClient
				.get()
				.uri("planets/" + name)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(PlanetDTO.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public <T> Mono<T> findByUrl(String url, Class<T> tipo) {
		log.info("Buscando o filme de id: [{}] pelo findByUrl", url);
		return swapiClient
				.get()
				.uri(url)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(tipo)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public Mono<Film> fetchFilmByUrl(String filmUrl) {
		return swapiClient
				.get()
				.uri(filmUrl)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Film.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}

	public Mono<Character> fetchCharacterByUrl(String characterUrl) {
		return swapiClient
				.get()
				.uri(characterUrl)
				.retrieve()
				.bodyToMono(Character.class)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}
}
