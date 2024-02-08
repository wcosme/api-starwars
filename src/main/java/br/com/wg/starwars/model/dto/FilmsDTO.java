package br.com.wg.starwars.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmsDTO implements Serializable{

	private UUID id;
	private String url;
	private String title;
	private Integer episode_id;
	private String opening_crawl;
	private LocalDate release_date;
}
