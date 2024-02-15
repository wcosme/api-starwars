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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
				.flatMap(this::fetchCharactersForFilm)
				.retryWhen(Retry.fixedDelay(3, Duration.ofMillis(1000)));
	}


	private Mono<Film> fetchCharactersForFilm(Film film) {
		List<Mono<Character>> characterMonos = film.getCharacters().stream()
				.map(characterUrl -> WebClient.create()
						.get()
						.uri(characterUrl)
						.retrieve()
						.bodyToMono(Character.class))
				.collect(Collectors.toList());

		return Mono.zip(characterMonos, characters -> {
			List<Character> characterList = Arrays.asList(characters)
					.stream()
					.map(character -> (Character) character)
					.collect(Collectors.toList());
			film.setCharacter(characterList);
			return film;
		});
	}
}
