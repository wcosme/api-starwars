package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.client.SwapiClient;
import br.com.wg.starwars.mapper.PlanetMapper;
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

        /*return planetRepository.findById(id)
                .switchIfEmpty(fetchPlanetFromExternalApi(id))
                .onErrorResume(throwable -> handleNotFound(Mono.empty(), id));*/

        return planetRepository.findById(id)
                .switchIfEmpty(swapiClient.findById(id)
                        .flatMap(planetDTO -> planetRepository.save(planetMapper.dtoToEntity(planetDTO))
                                .switchIfEmpty(handleNotFound(Mono.empty(), id))));
    }

    /*private Mono<Planet> fetchPlanetFromExternalApi(String id) {

        log.info("Buscando o planeta de id: [{}]", id);

        return swapiClient.findById(id)
                .flatMap(planetResponse -> {
                    //Planet planet = planetMapper.responseToEntity(planetResponse);
                    return fetchFilmsForPlanet(planetResponse); // Chama o método para buscar os filmes do planeta
                })
                .switchIfEmpty(handleNotFound(Mono.empty(), id));
    }*/


    /*private Mono<Planet> fetchFilmsForPlanet(PlanetDTO planetDTO) {
        List<Mono<Film>> filmMonos = new ArrayList<>();
        for (String filmUrl : planetDTO.getFilms()) {
            log.info("Buscando o filme de url: [{}]", filmUrl);
            filmMonos.add(swapiClient.fetchFilmByUrl(filmUrl));
        }
        return Mono.zip(filmMonos, films -> {
            Planet planet = planetMapper.dtoToEntity(planetDTO);
            for (Object film : films) {
                planet.addFilm((Film) film);
            }
            return planet;
        });
    }*/

    /*private Mono<Planet> fetchFilmsForPlanet(PlanetDTO planetDTO) {
        List<Film> films = planetMapper.mapFilms(planetDTO.getFilms());
        Planet planet = planetMapper.dtoToEntity(planetDTO);
        planet.setFilms(films);
        return Mono.just(planet);
    }*/





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

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id) {
        return mono.switchIfEmpty(Mono.error(
                new ObjectNotFoundException(
                        format("Object not found. Id: %s, Type: %s", id, Planet.class.getSimpleName())
                )
        ));
    }

    /*private Flux<Planet> getPlanet(PlanetDTO dto) {
        var planetFlux = Flux.just(dto);

        var filmsFlux = Flux.fromIterable(dto.getFilms())
                .flatMap(url -> swapiClient.findByUrl(url, FilmsDTO.class))
                .map(filmsDTO -> new Film(
                        filmsDTO.getUrl(),
                        filmsDTO.getTitle(),
                        filmsDTO.getOpening_crawl()))
                .collectList();

        var result = planetFlux.zipWith(filmsFlux, (planet, films) -> {
            return new Planet(UUID.randomUUID().toString(), planet.getName(), planet.getClimate(), planet.getTerrain(), planet.getFilmAppearances(), planet.getFilms());
        });

        return result;
    }*/
}
