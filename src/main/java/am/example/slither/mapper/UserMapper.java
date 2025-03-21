package am.example.slither.mapper;

import am.example.slither.dto.UserLoginDto;
import am.example.slither.dto.UserRegisterDto;
import am.example.slither.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToRegister(UserRegisterDto dto);

    User mapToLogin(UserLoginDto dto);
}
