package am.example.slither.mapper;

import am.example.slither.dto.SnakeDto;
import am.example.slither.entity.Snake;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SnakeMapper {

    @Mapping(target = "id", source = "id")
    Snake mapToSnake(SnakeDto dto);

    @Mapping(target = "id", source = "id")
    SnakeDto mapToDto(Snake entity);
}
