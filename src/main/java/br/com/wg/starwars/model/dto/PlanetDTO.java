package br.com.wg.starwars.model.dto;

import br.com.wg.starwars.model.document.Film;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetDTO {
    private String id;
	private String name;
    private String climate;
    private String terrain;
    private Long filmAppearances;
    private List<String> films;
    private List<Film> filmes = new ArrayList<>();

    public List<Film> getFilmes() {
        return filmes;
    }

    public void setFilmes(List<Film> filmes) {
        this.filmes = filmes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public Long getFilmAppearances() {
        if(this.getFilms() != null){
            return this.filmAppearances = (long) this.getFilms().size();
        }
        return this.filmAppearances = 0L;
    }

    public void setFilmAppearances(Long filmAppearances) {
        this.filmAppearances = filmAppearances;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }
}
