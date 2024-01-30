package br.com.wg.starwars.controllers.impl;

import br.com.wg.starwars.controllers.PlanetController;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.model.response.PlanetResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PlanetControllerImpl implements PlanetController {

    @Override
    public ResponseEntity<Mono<Void>> save(PlanetRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<PlanetResponse>> findById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<PlanetResponse>> findByName(String name) {
        return null;
    }

    @Override
    public ResponseEntity<Flux<PlanetResponse>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
