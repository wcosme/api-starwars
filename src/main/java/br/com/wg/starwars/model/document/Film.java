package br.com.wg.starwars.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

	private String title;
	private String director;
	private String opening_crawl;
	private List<String> characters;
	private List<Character> character;
}
