package ece651.RISC.Server.EVO2Controller;

import com.alibaba.fastjson2.JSON;
import ece651.RISC.Server.Model.Payload.Request.CreateUserRequest;
import ece651.RISC.Server.Model.Payload.Request.LoginUserRequest;
import ece651.RISC.Server.Model.Payload.Response.CreateUserResponse;
import ece651.RISC.Server.Model.Payload.Response.LoginUserResponse;
import ece651.RISC.Server.Model.User;
import ece651.RISC.Server.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is a controller class for user signup and login
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public UserRepository userRepository;

    private long userIDCounter = 0;

    /**
     * This method handles the sign up request of a new user. It creates a new User object with the username and password
     * provided in the CreateUserRequest and attempts to add it to the UserRepository. If the addition is successful,
     * a response containing the new user's ID is returned with a HTTP status code of 201 (Created). If the addition
     * fails, a HTTP status code of 400 (Bad Request) is returned.
     *
     * @param createUserRequest The CreateUserRequest object containing the username and password of the new user.
     * @return A ResponseEntity object containing the response and HTTP status code.
     */
    @PostMapping("/signup")
    public synchronized ResponseEntity<CreateUserResponse> signUp(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getUsername(), createUserRequest.getPassword());
        // try to add the user to the database
        String msg = userRepository.tryAddUser(userIDCounter, user);
        if (msg == null) {
            String createUserRequestJsonString = JSON.toJSONString(createUserRequest);
            LOGGER.info("New user created: " + createUserRequestJsonString);
            CreateUserResponse response = new CreateUserResponse(userIDCounter);
            userIDCounter++;
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            LOGGER.info(msg);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handle POST request to login a user with given username and password.
     *
     * @param loginUserRequest JSON object containing the login information.
     * @return ResponseEntity containing the LoginUserResponse and HTTP status code.
     */

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        String username = loginUserRequest.getUsername();
        String password = loginUserRequest.getPassword();

        User user = userRepository.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        LoginUserResponse response = new LoginUserResponse(user.getId(), user.getUsername(), true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping("/user")
//    public  ResponseEntity<LoginUserResponse> user() {
//        // TODO 根据session返回user info
//    }
//
//    @GetMapping("/logout")
//    public  ResponseEntity<LoginUserResponse> user() {
//        // TODO 根据session返回user info loggedIn变成false
//    }
}