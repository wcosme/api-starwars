package br.com.wg.starwars.controllers.impl;

import br.com.wg.starwars.mapper.PlanetMapper;
import br.com.wg.starwars.model.document.Film;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
class PlanetControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlanetService planetService;

    @MockBean
    private PlanetMapper planetMapper;

    @Test
    public void testFindById() {
        // Mock dos dados do planeta
        Planet planet = new Planet();
        planet.setId("1");
        planet.setName("Tatooine");
        planet.setClimate("Arid");
        planet.setTerrain("Desert");
        planet.setFilmAppearances(5L);
        planet.setFilms(Arrays.asList("http://url1.com", "http://url2.com"));

        // Mock dos filmes associados ao planeta
        List<Film> films = Arrays.asList(
                new Film("A New Hope", "George Lucas", "Opening crawl 1", Collections.emptyList(), Collections.emptyList()),
                new Film("The Empire Strikes Back", "Irvin Kershner", "Opening crawl 2", Collections.emptyList(), Collections.emptyList())
        );
        planet.setFilmes(films);

        // Configurando comportamento do serviço mockado
        when(planetService.findById(anyString())).thenReturn(Mono.just(planet));


        // Realizando a chamada HTTP e verificando a resposta
        webTestClient.get()
                .uri("/planets/{id}", planet.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

}