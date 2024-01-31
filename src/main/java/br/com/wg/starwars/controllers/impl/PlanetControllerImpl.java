package br.com.wg.starwars.controllers.impl;

import br.com.wg.starwars.controllers.PlanetController;
import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.model.response.PlanetResponse;
import br.com.wg.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/planets")
public class PlanetControllerImpl implements PlanetController {

    private final PlanetService service;
    private final PlanetMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(final PlanetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<PlanetResponse>> findById(String id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.findById(id).map(mapper::entityToResponse)
        );
    }

    @Override
    public ResponseEntity<Mono<PlanetResponse>> findByName(String name) {
        return null;
    }

    @Override
    public ResponseEntity<Flux<PlanetResponse>> findAll() {
        return ResponseEntity.ok().body(
                service.findAll().map(mapper::entityToResponse)
        );
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return ResponseEntity.ok().body(
                service.delete(id).then()
        );
    }
}
