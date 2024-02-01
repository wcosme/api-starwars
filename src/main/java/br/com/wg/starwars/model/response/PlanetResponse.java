package br.com.wg.starwars.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetResponse(
        String id,
        String name,
        String climate,
        String terrain,
        Long numberAppearances,
        List<String> films
) { }
