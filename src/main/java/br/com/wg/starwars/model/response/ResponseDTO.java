package br.com.wg.starwars.model.response;

import br.com.wg.starwars.model.dto.PlanetDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseDTO(
        String next,
        List<PlanetDTO> results)
    {
}
