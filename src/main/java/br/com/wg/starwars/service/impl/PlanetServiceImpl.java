package br.com.wg.starwars.service.impl;

import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.repository.PlanetRepository;
import br.com.wg.starwars.service.PlanetService;
import br.com.wg.starwars.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;

    @Override
    public Mono<Planet> save(PlanetRequest planetRequest) {
        return planetRepository.save(planetMapper.requestToEntity(planetRequest));
    }

    @Override
    public Mono<Planet> findById(String id) {
        return handleNotFound(planetRepository.findById(id), id);
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
}
