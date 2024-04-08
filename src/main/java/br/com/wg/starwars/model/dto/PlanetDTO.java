package br.com.wg.starwars.model.dto;

import br.com.wg.starwars.model.document.Film;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
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


    public Long getFilmAppearances() {
        if(this.getFilms() != null){
            return this.filmAppearances = (long) this.getFilms().size();
        }
        return this.filmAppearances = 0L;
    }
}
