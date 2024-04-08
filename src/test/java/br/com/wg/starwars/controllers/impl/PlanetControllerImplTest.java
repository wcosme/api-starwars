package br.com.wg.starwars.controllers.impl;

import br.com.wg.starwars.model.document.Character;
import br.com.wg.starwars.model.document.Film;
import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.dto.PlanetDTO;
import br.com.wg.starwars.service.PlanetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class PlanetControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlanetService planetService;

    private PlanetDTO planetDTO;



    @BeforeEach
    public void setUp() {
        // Mock dos dados do planeta
        planetDTO = new PlanetDTO();
        planetDTO.setId("1");
        planetDTO.setName("Tatooine");
        planetDTO.setClimate("Arid");
        planetDTO.setTerrain("Desert");
        planetDTO.setFilmAppearances(5L);
        planetDTO.setFilms(Arrays.asList("http://url1.com", "http://url2.com"));

        // Mock dos filmes associados ao planeta
        List<Film> films = new ArrayList<>();
        films.add(createFilm("A New Hope", "George Lucas", "Opening crawl 1", Arrays.asList("Luke Skywalker", "Princess Leia")));
        films.add(createFilm("The Empire Strikes Back", "Irvin Kershner", "Opening crawl 2", Arrays.asList("Darth Vader", "Han Solo")));
        planetDTO.setFilmes(films);

    }



    @Test
    public void testFindById() {

        Planet expectedPlanet = createPlanetEntity(planetDTO);

        when(planetService.findById(anyString())).thenReturn(Mono.just(expectedPlanet));


        // Realizando a chamada HTTP e validando a resposta
        webTestClient.get()
                .uri("/planets/{id}", planetDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PlanetDTO.class)
                .consumeWith(response -> {
                    PlanetDTO planetResponse = response.getResponseBody();
                    assertThat(planetResponse).isNotNull();
                    assertThat(planetResponse.getId()).isEqualTo(expectedPlanet.getId());
                    assertThat(planetResponse.getName()).isEqualTo(expectedPlanet.getName());
                    assertThat(planetResponse.getClimate()).isEqualTo(expectedPlanet.getClimate());
                    assertThat(planetResponse.getTerrain()).isEqualTo(expectedPlanet.getTerrain());
                });
    }


    private Film createFilm(String title, String director, String openingCrawl, List<String> characterNames) {
        Film film = new Film();
        film.setTitle(title);
        film.setDirector(director);
        film.setOpening_crawl(openingCrawl);

        List<Character> characters = new ArrayList<>();
        for (String characterName : characterNames) {
            characters.add(createCharacter(characterName));
        }
        film.setCharacter(characters);
        return film;
    }

    private Character createCharacter(String name) {
        Character character = new Character();
        character.setName(name);
        return character;
    }

    private Planet createPlanetEntity(PlanetDTO planetDTO) {
        Planet planet = new Planet();
        planet.setId(planetDTO.getId());
        planet.setName(planetDTO.getName());
        planet.setClimate(planetDTO.getClimate());
        planet.setTerrain(planetDTO.getTerrain());
        planet.setFilmAppearances(planetDTO.getFilmAppearances());
        planet.setFilms(planetDTO.getFilms());
        planet.setFilmes(planetDTO.getFilmes());

        return planet;
    }

}