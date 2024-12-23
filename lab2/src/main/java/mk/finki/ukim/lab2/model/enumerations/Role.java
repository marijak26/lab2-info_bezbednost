package mk.finki.ukim.lab2.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER, ROLE_MANAGER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
