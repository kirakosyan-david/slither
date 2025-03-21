package am.example.slither.service;

public interface UserService {

    boolean registerUser(String username, String password, String email);
    boolean loginUser(String password, String email);
}
