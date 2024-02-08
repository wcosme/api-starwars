package br.com.wg.starwars.repository;

import br.com.wg.starwars.client.ClientAPI;
import br.com.wg.starwars.model.document.Film;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.dto.FilmsDTO;
import br.com.wg.starwars.model.dto.PlanetDTO;
import br.com.wg.starwars.model.response.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@AllArgsConstructor
@Repository
public class WebClientRepository {

    private final ClientAPI api;


    public Flux<Planet> all() {

        var fluxPlanets = api.exec("https://swapi.dev/api/planets", ResponseDTO.class)
                .expand(expander -> api.exec(expander.next(), ResponseDTO.class))
                .flatMapIterable(r -> {
                    return r.results();
                })
                .flatMap(this::FluxCreatePlanets);

        return fluxPlanets;

    }

    private Flux<Planet> FluxCreatePlanets(PlanetDTO dto) {
        var dtoFlux = Flux.just(dto);

        var fluxFilm = Flux.fromIterable(dto.films())
                .flatMap(url -> api.exec(url, FilmsDTO.class))
                .cache(Duration.ofMinutes(5))
                .collectList();

        var result = dtoFlux.zipWith(fluxFilm, (p, f) -> {
            var filmes = f.stream().map(filmDTO -> new Film(
                            filmDTO.getUrl(),
                            filmDTO.getTitle(),
                            filmDTO.getEpisode_id(),
                            filmDTO.getOpening_crawl(),
                            filmDTO.getRelease_date()))
                    .toList();
            return new Planet(p.id(), p.name(), p.climate(), p.terrain(), p.films());
        });

        return result;
    }

    public Mono<Planet> byId(String id) {
        return api.exec(String.format("https://swapi.dev/api/planets/%d/", id), PlanetDTO.class)
                .flatMap(d -> Mono.from(FluxCreatePlanets(d)));
    }
}
