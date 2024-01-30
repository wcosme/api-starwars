package br.com.wg.starwars.repository;

import br.com.wg.starwars.model.document.Planet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PlanetRepository extends ReactiveMongoRepository<Planet, String> {
    Flux<Planet> findByNameContainingIgnoreCase(String name);
}
