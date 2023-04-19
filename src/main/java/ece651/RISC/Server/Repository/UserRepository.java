package ece651.RISC.Server.Repository;

import ece651.RISC.Server.Model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private final Map<String, Long> usernameToIdMap = new HashMap<>();

    public void addUser(long userId, User user) {
        user.setId(userId);
        userMap.put(userId, user);
        usernameToIdMap.put(user.getUsername(), userId);
    }

    public User getUser(long userId) {
        return userMap.get(userId);
    }

    public User getUserByUsername(String username) {
        Long userId = usernameToIdMap.get(username);
        if (userId == null) {
            return null;
        }
        return getUser(userId);
    }
}