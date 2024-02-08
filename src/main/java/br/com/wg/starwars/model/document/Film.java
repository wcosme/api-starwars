package br.com.wg.starwars.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="films")
public class Film implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String title;
	private Integer episode_id;
	private String opening_crawl;
	private LocalDate release_date;
}
