package br.com.wg.starwars.model.request;

public record PlanetRequest(
        String name,
        String climate,
        String terrain
) { }
