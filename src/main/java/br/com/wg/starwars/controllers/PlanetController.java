package br.com.wg.starwars.controllers;

import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.model.response.PlanetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetController {

    @PostMapping
    ResponseEntity<Mono<Void>> save(@RequestBody PlanetRequest request);

    @GetMapping(value = "/{id}")
    ResponseEntity<Mono<PlanetResponse>> findById(@PathVariable String id);

    @GetMapping(value = "/{name}")
    ResponseEntity<Mono<PlanetResponse>> findByName(@PathVariable String name);

    @GetMapping
    ResponseEntity<Flux<PlanetResponse>> findAll();

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Mono<Void>> delete(@PathVariable String id);
}
