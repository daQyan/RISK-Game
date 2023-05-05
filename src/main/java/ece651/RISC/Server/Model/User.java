package ece651.RISC.Server.Model;

import lombok.Data;
/**
 * The User class represents a user in the system.
 * Each user has a unique id, a username, and a password.
 */
@Data
public class User {
    private long id;
    private String username;
    private String password;

    /**
     * Constructs a new User object with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}