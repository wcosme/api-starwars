package br.com.wg.starwars.model.response;

public record PlanetResponse(
        String id,
        String name,
        String climate,
        String terrain,
        Long numberAppearances
) { }
