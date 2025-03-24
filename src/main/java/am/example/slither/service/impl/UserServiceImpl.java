package am.example.slither.service.impl;


import am.example.slither.dto.UserLoginDto;
import am.example.slither.dto.UserRegisterDto;
import am.example.slither.entity.User;
import am.example.slither.mapper.UserMapper;
import am.example.slither.repository.UserRepository;
import am.example.slither.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public boolean registerUser(String username, String password, String email) {
        validateUsernameUniqueness(username);

        validateEmailUniqueness(email);

        User user = userMapper.mapToRegister(UserRegisterDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build());
        userRepository.save(user);
        return true;
    }


    @Override
    public boolean loginUser(String password, String email) {
        Optional<User> loginEmailAndPassword = userRepository.findByEmailAndPassword(email, password);
        return loginEmailAndPassword.isPresent();
    }


    private void validateUsernameUniqueness(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }
}
