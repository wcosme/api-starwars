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

    //@Mapping(target = "id", ignore = true)
    Planet requestToEntity(final PlanetRequest request);

    //@Mapping(target = "id", ignore = true)
    Planet dtoToEntity(final PlanetDTO planet);

    @Mappings({
            //@Mapping(target = "id", ignore = true),
            //@Mapping(target = "actions", source = "childSource")
    })
    PlanetDTO entityToDto(final Planet entity);

}
