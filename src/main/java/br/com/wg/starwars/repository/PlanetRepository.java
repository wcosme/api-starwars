package br.com.wg.starwars.repository;

import br.com.wg.starwars.model.document.Planet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends ReactiveMongoRepository<Planet, String> {

}
