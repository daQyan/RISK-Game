package ece651.RISC.Server.Repository;

import ece651.RISC.Server.Model.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * This class represents a repository for storing and accessing User objects in the system.
 * User objects are stored in a LinkedHashMap with the user ID as the key and the User object as the value.
 * A separate LinkedHashMap is used to store the mapping of username to user ID.
 * This class provides methods to add new users, check if a user already exists, get a user by ID, and get a user by username.
 */
@Repository
public class UserRepository {
    private final Map<Long, User> userMap = new LinkedHashMap<>();
    private final Map<String, Long> usernameToIdMap = new LinkedHashMap<>();

    /**
     * Adds a new user to the repository if the username does not already exist.
     *
     * @param userId The ID to assign to the new user.
     * @param user   The User object to add to the repository.
     * @return A String indicating an error message if the username already exists, or null if the user was added successfully.
     */
    public String tryAddUser(long userId, User user) {
        if (checkIfUserExist(user.getUsername())) {
            return "Username already exists";
        }

        user.setId(userId);
        userMap.put(userId, user);
        usernameToIdMap.put(user.getUsername(), userId);
        return null;
    }

    /**
     * Checks if a user with the given username already exists in the repository.
     *
     * @param userName The username to check.
     * @return True if a user with the given username already exists in the repository, false otherwise.
     */
    public boolean checkIfUserExist(String userName) {
        return usernameToIdMap.containsKey(userName);
    }

    /**
     * Returns the User object with the given ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object with the given ID, or null if no such user exists.
     */

    public User getUser(long userId) {
        return userMap.get(userId);
    }

    /**
     * Returns the User object with the given username.
     *
     * @param username The username of the user to retrieve.
     * @return The User object with the given username, or null if no such user exists.
     */
    public User getUserByUsername(String username) {
        Long userId = usernameToIdMap.get(username);
        if (userId == null) {
            return null;
        }
        return getUser(userId);
    }
}