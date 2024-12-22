package mk.finki.ukim.lab2.service.impl;

import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.model.exceptions.InvalidArgumentsException;
import mk.finki.ukim.lab2.model.exceptions.InvalidUserCredentialsException;
import mk.finki.ukim.lab2.repository.JpaUserRepository;
import mk.finki.ukim.lab2.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JpaUserRepository userRepository;

    public AuthServiceImpl(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }
}
