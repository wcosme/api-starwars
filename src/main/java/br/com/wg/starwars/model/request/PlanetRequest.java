package br.com.wg.starwars.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetRequest(
        String id,
        String name,
        String climate,
        String terrain,
        Long numberAppearances,
        String createDate,
        String updated
) { }