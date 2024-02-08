package br.com.wg.starwars.controllers;

import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.model.dto.PlanetDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetController {

    @PostMapping
    ResponseEntity<Mono<PlanetDTO>> save(@Valid @RequestBody PlanetRequest request);

    @GetMapping(value = "/id/{id}")
    ResponseEntity<Mono<PlanetDTO>> findById(@PathVariable String id);

    @GetMapping(value = "/planetName/{name}")
    ResponseEntity<Flux<PlanetDTO>> findByName(@PathVariable("name") String name);

    @GetMapping
    ResponseEntity<Flux<PlanetDTO>> findAll();

    @DeleteMapping(value = "/id/{id}")
    ResponseEntity<Mono<Void>> delete(@PathVariable String id);
}
