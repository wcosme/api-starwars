package br.com.wg.starwars.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {

	private String title;
	private String director;
	private String opening_crawl;
}
