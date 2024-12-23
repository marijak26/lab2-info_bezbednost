package mk.finki.ukim.lab2.service.impl;

import jakarta.mail.MessagingException;
import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.model.enumerations.Role;
import mk.finki.ukim.lab2.model.exceptions.EmailSendingException;
import mk.finki.ukim.lab2.model.exceptions.InvalidArgumentsException;
import mk.finki.ukim.lab2.model.exceptions.PasswordsDoNotMatchException;
import mk.finki.ukim.lab2.model.exceptions.UsernameAlreadyExistsException;
import mk.finki.ukim.lab2.repository.JpaUserRepository;
import mk.finki.ukim.lab2.service.EmailService;
import mk.finki.ukim.lab2.service.UserService;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(JpaUserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role role) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        User user = new User(username, passwordEncoder.encode(password), name, surname, role);

        user.setVerified(false);
        user.setEnabled(false);
        userRepository.save(user);

        String verificationLink = "http://localhost:8080/register/verify?email=" + user.getUsername();

        try {
            emailService.sendVerificationEmail(user.getUsername(), verificationLink);
        } catch (MessagingException | MailException e) {
            throw new EmailSendingException("Failed to send verification email", e);
        }

        return user;
    }

    private String generateVerificationToken(String username) {
        return UUID.randomUUID().toString() + "_" + username;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void verifyUser(String token) {
        String username = extractUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        user.setVerified(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String extractUsernameFromToken(String token) {
        String[] parts = token.split("_");
        return parts.length > 1 ? parts[1] : null;
    }
}

