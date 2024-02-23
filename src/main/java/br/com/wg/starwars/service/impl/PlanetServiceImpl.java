package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.client.SwapiClient;
import br.com.wg.starwars.mapper.PlanetMapper;
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
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

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
        if (planet.getFilms() == null) {
            planet.setFilms(new ArrayList<>());
        }

        // Criar um Flux de chamadas assíncronas para buscar os filmes
        Flux<Film> filmsFlux = Flux.fromIterable(planet.getFilms())
                .parallel() // Converte para ParallelFlux
                .runOn(Schedulers.parallel()) // Executa em threads paralelas
                .flatMap(swapiClient::fetchFilmByUrl)
                .flatMap(film -> fetchCharactersForFilm(film).thenReturn(film)) // Chamada para buscar os personagens de cada filme
                .sequential(); // Converte de volta para Flux sequencial;


        // Coletar os resultados em uma lista
        return filmsFlux.collectList()
                .flatMap(films -> {
                    planet.setFilmes(films);
                    return planetRepository.save(planet)
                            .doOnSuccess(savedPlanet -> log.info("Planeta salvo com sucesso: {}", savedPlanet))
                            .doOnError(error -> log.error("Erro ao salvar planeta: {}", error.getMessage()));
                });


    }

    private Mono<Film> fetchCharactersForFilm(Film film) {
        // Buscar os personagens associados ao filme
        return Flux.fromIterable(film.getCharacters())
                .parallel() // Converte para ParallelFlux
                .runOn(Schedulers.parallel()) // Executa em threads paralelas
                .flatMap(swapiClient::fetchCharacterByUrl)
                .sequential() // Converte de volta para Flux sequencial
                .collectList()
                .map(characters -> {
                    film.setCharacter(characters);
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
