package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.client.SwapiClient;
import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.document.Film;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.repository.PlanetRepository;
import br.com.wg.starwars.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetEnrichmentService {

    private final SwapiClient swapiClient;
    private final PlanetMapper planetMapper;
    private final PlanetRepository planetRepository;

    public Mono<Planet> fetchAndEnrich(String id) {
        return swapiClient.findById(id)
                .flatMap(dto -> {
                    Planet planet = planetMapper.dtoToEntity(dto);
                    log.info("Planeta encontrado na SWAPI: {}", planet.getName());
                    return enrichWithFilmsAndCharacters(planet);
                })
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(
                        format("Object not found. Id: %s, Type: %s", id, Planet.class.getSimpleName())
                )));
    }

    private Mono<Planet> enrichWithFilmsAndCharacters(Planet planet) {
        if (planet.getFilms() == null) {
            planet.setFilms(new ArrayList<>());
        }

        Flux<Film> filmsFlux = Flux.fromIterable(planet.getFilms())
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(swapiClient::fetchFilmByUrl)
                .flatMap(film -> fetchCharactersForFilm(film).thenReturn(film))
                .sequential();

        return filmsFlux.collectList()
                .flatMap(films -> {
                    planet.setFilmes(films);
                    return planetRepository.save(planet)
                            .doOnSuccess(saved -> log.info("Planeta salvo com sucesso: {}", saved.getName()))
                            .doOnError(error -> log.error("Erro ao salvar planeta: {}", error.getMessage()));
                });
    }

    private Mono<Film> fetchCharactersForFilm(Film film) {
        return Flux.fromIterable(film.getCharacters())
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(swapiClient::fetchCharacterByUrl)
                .sequential()
                .collectList()
                .map(characters -> {
                    film.setCharacter(characters);
                    return film;
                });
    }
}
