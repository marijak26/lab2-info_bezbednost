package mk.finki.ukim.lab2.service;

import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.model.enumerations.Role;

import java.util.List;

public interface AuthService {

    User login(String username, String password);
}



