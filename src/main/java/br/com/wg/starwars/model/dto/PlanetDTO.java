package br.com.wg.starwars.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetDTO(

        String id,
        String name,
        String climate,
        String terrain,
        List<String> films
) {
}
