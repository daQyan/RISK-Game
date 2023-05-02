package ece651.RISC.Server.Repository;

import ece651.RISC.Server.Model.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Long, User> userMap = new LinkedHashMap<>();
    private final Map<String, Long> usernameToIdMap = new LinkedHashMap<>();

    public String tryAddUser(long userId, User user) {
        if(checkIfUserExist(user.getUsername())) {
            return "Username already exists";
        }

        user.setId(userId);
        userMap.put(userId, user);
        usernameToIdMap.put(user.getUsername(), userId);
        return null;
    }

    public boolean checkIfUserExist(String userName) {
        return usernameToIdMap.containsKey(userName);
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