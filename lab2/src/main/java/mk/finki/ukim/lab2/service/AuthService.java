package mk.finki.ukim.lab2.service;

import mk.finki.ukim.lab2.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    private Map<String, User> users = new HashMap<>();
    private Map<String, String> verificationCodes = new HashMap<>();
    private Map<String, String> twoFactorCodes = new HashMap<>();

    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void saveVerificationCode(String username, String verificationCode) {
        verificationCodes.put(username, verificationCode);
    }

    public String getVerificationCode(String username) {
        return verificationCodes.get(username);
    }

    public boolean isEmailRegistered(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean verifyPassword(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public String generateTwoFactorCode(String username) {
        String code = String.format("%06d", (int)(Math.random() * 1000000));
        twoFactorCodes.put(username, code);

        return code;
    }

    public boolean verifyTwoFactorCode(String username, String enteredCode) {
        String generatedCode = twoFactorCodes.get(username);
        return generatedCode != null && generatedCode.equals(enteredCode);
    }

    public void clearTwoFactorCode(String username) {
        twoFactorCodes.remove(username);
    }

    public void verifyUser(String username) {
        User user = users.get(username);
        if (user != null) {
            user.setVerified(true);
        }
    }

    public boolean isUserVerified(String username) {
        User user = users.get(username);
        return user != null && user.isVerified();
    }
}



