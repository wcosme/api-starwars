package br.com.wg.starwars.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="planets")
public class Planet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String name;
	private String climate;
	private String terrain;
	private Long filmAppearances;
	private List<String> films;
	private List<Film> filmes = new ArrayList<>();

	public void addFilm(Film film) {
		this.filmes.add(film);
	}
}
