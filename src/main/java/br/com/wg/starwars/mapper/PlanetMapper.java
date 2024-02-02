package br.com.wg.starwars.mapper;

import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.request.PlanetRequest;
import br.com.wg.starwars.model.response.PlanetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface PlanetMapper {

    @Mapping(target = "id", ignore = true)
    Planet requestToEntity(final PlanetRequest request);

    Planet responseToEntity(final PlanetResponse planet);

    PlanetResponse entityToResponse(final Planet entity);
}
