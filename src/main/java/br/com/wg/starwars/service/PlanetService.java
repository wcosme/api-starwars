package br.com.wg.starwars.service;

import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.request.PlanetRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetService {
    Mono<Planet> create(PlanetRequest planetRequest);
    Mono<Planet> findById(String id);
    Flux<Planet> findByName(String name);
    Flux<Planet> findAll();
    Mono<Void> delete(String id);
}
