package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.repository.PlanetRepository;
import br.com.wg.starwars.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;

    @Override
    public Mono<Planet> save(PlanetRequest planetRequest) {
        return null;
    }

    @Override
    public Mono<Planet> findById(String id) {
        return null;
    }

    @Override
    public Flux<Planet> findByName(String name) {
        return planetRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Flux<Planet> findAll() {
        return planetRepository.findAll();
    }

    @Override
    public Mono<Void> delete(String id) {
        return planetRepository.findById(id)
                .flatMap(planetRepository::delete)
                .then();
    }
}
