package br.com.wg.starwars.mapper;

import br.com.wg.starwars.model.document.Planet;
import br.com.wg.starwars.model.dto.PlanetDTO;
import br.com.wg.starwars.model.request.PlanetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface PlanetMapper {


    Planet requestToEntity(final PlanetRequest request);

    //@Mapping(target = "filmes", ignore = true)
    Planet dtoToEntity(final PlanetDTO planet);

    @Mappings({
            //@Mapping(target = "films", ignore = true),
            //@Mapping(target = "id", ignore = true)
    })
    PlanetDTO entityToDto(final Planet entity);

}
