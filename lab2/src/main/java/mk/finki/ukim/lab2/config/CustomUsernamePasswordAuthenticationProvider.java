package mk.finki.ukim.lab2.config;

import mk.finki.ukim.lab2.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Predefined users with roles and plain-text passwords
    private static final Map<String, UserDetails> predefinedUsers = new HashMap<>();

    static {
        predefinedUsers.put("user", new UserDetailsImpl("user", "user", "ROLE_USER"));
        predefinedUsers.put("admin", new UserDetailsImpl("admin", "admin", "ROLE_ADMIN"));
        predefinedUsers.put("manager", new UserDetailsImpl("manager", "manager", "ROLE_MANAGER"));
    }

    public CustomUsernamePasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        // Encode the plain-text passwords at runtime
        encodePredefinedPasswords();
    }

    // Encode the plain-text passwords using PasswordEncoder
    private void encodePredefinedPasswords() {
        for (String username : predefinedUsers.keySet()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) predefinedUsers.get(username);
            String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
            userDetails.setPassword(encodedPassword); // Update with encoded password
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Empty credentials!");
        }

        // Retrieve the user from predefined users
        UserDetails userDetails = predefinedUsers.get(username);

        if (userDetails == null) {
            throw new BadCredentialsException("User not found!");
        }

        // Check if the password matches the encoded one
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
