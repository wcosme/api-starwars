package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.client.SwapiClient;
import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.document.Character;
import br.com.wg.starwars.model.document.Film;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.repository.PlanetRepository;
import br.com.wg.starwars.service.PlanetService;
import br.com.wg.starwars.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
@Slf4j
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;
    private final SwapiClient swapiClient;

    @Override
    public Mono<Planet> save(PlanetRequest planetRequest) {
        return planetRepository.save(planetMapper.requestToEntity(planetRequest));
    }

    @Override
    public Mono<Planet> findById(String id) {
        return planetRepository.findById(id)
                .switchIfEmpty(fetchPlanetFromExternalApi(id))
                .onErrorResume(throwable -> handleNotFound(Mono.empty(), id));


    }

    @Override
    public Flux<Planet> findByName(String name) {
        return planetRepository.findByNameContainingIgnoreCase(name)
                .switchIfEmpty(handleNotFound(Mono.empty(), name));

    }

    @Override
    public Flux<Planet> findAll() {
        return planetRepository.findAll();
    }

    @Override
    public Mono<Void> delete(String id) {
        return handleNotFound(planetRepository.findById(id), id)
                .flatMap(planetRepository::delete)
                .then();
    }

    private Mono<Planet> fetchPlanetFromExternalApi(String id) {
        return swapiClient.findById(id)
                .flatMap(dto -> {
                    Planet planet = planetMapper.dtoToEntity(dto);
                    log.info("Planeta encontrado: {}", planet);
                    return fetchFilmsAndCharactersForPlanet(planet)
                            .switchIfEmpty(Mono.error(new RuntimeException("Erro ao buscar filmes para o planeta.")))
                            .thenReturn(planet); // Chama o método para buscar os filmes do planeta
                })
                .switchIfEmpty(handleNotFound(Mono.empty(), id));
    }


    private Mono<Planet> fetchFilmsAndCharactersForPlanet(Planet planet) {
        List<Mono<Film>> filmMonos = new ArrayList<>();
        if (planet.getFilms() == null) {
            planet.setFilms(new ArrayList<>());
        }
        for (String filmUrl : planet.getFilms()) {
            filmMonos.add(swapiClient.fetchFilmByUrl(filmUrl)
                    .flatMap(film -> fetchCharactersForFilm(film) // Chamada para buscar os personagens de cada filme
                            .thenReturn(film))
                    .doOnSuccess(film -> {
                        planet.addFilm(film); // Adiciona o filme à lista de filmes do planeta
                        log.info("Filme {} adicionado ao planeta {}", film, planet);
                    })
                    .doOnError(error -> log.error("Erro ao buscar filme para a URL: {}", filmUrl, error)));
        }
        return Mono.zip(filmMonos, films -> planet)
                .flatMap(updatedPlanet -> planetRepository.save(updatedPlanet)
                        .doOnSuccess(savedPlanet -> log.info("Planeta salvo com sucesso: {}", savedPlanet))
                        .doOnError(error -> log.error("Erro ao salvar planeta: {}", error.getMessage())));
    }

    private Mono<Film> fetchCharactersForFilm(Film film) {
        List<Mono<Character>> characterMonos = film.getCharacters().stream()
                .map(swapiClient::fetchCharacterByUrl) // Chamada para buscar os personagens de um filme
                .collect(Collectors.toList());

        return Flux.concat(characterMonos)
                .collectList()
                .map(characters -> {
                    film.setCharacter(characters); // Define os personagens no filme
                    return film;
                });
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id) {
        return mono.switchIfEmpty(Mono.error(
                new ObjectNotFoundException(
                        format("Object not found. Id: %s, Type: %s", id, Planet.class.getSimpleName())
                )
        ));
    }
}
