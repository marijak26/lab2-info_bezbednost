package mk.finki.ukim.lab2.config;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
public class UserDetailsImpl extends User {
    // Add setter for password
    private String password;

    public UserDetailsImpl(String username, String password, String role) {
        super(username, password, AuthorityUtils.createAuthorityList(role));
        this.password = password;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
