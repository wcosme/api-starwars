package br.com.wg.starwars.controllers.impl;

import br.com.wg.starwars.controllers.PlanetController;
import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.dto.PlanetDTO;
import br.com.wg.starwars.model.request.PlanetRequest;
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
    public ResponseEntity<Mono<PlanetDTO>> save(final PlanetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.save(request).map(mapper::entityToDto)
        );
    }

    @Override
    public ResponseEntity<Mono<PlanetDTO>> findById(String id) {
        return ResponseEntity.ok().body(
                service.findById(id).map(mapper::entityToDto)
        );
    }

    @Override
    public ResponseEntity<Flux<PlanetDTO>> findByName(String name) {
        return ResponseEntity.ok().body(
                service.findByName(name).map(mapper::entityToDto)
        );
    }

    @Override
    public ResponseEntity<Flux<PlanetDTO>> findAll() {
        return ResponseEntity.ok().body(
                service.findAll().map(mapper::entityToDto)
        );
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                service.delete(id).then()
        );
    }
}
