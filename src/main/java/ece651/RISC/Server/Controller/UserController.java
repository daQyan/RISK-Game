package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import ece651.RISC.Server.Model.Payload.Request.CreateUserRequest;
import ece651.RISC.Server.Model.Payload.Request.LoginUserRequest;
import ece651.RISC.Server.Model.Payload.Response.CreateUserResponse;
import ece651.RISC.Server.Model.Payload.Response.LoginUserResponse;
import ece651.RISC.Server.Repository.UserRepository;
import ece651.RISC.Server.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public UserRepository userRepository;

    private long userIDCounter = 0;

    @PostMapping("/signup")
    public synchronized ResponseEntity<CreateUserResponse> signUp(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getUsername(), createUserRequest.getPassword());
        String createUserRequestJsonString = JSON.toJSONString(createUserRequest);
        System.out.println("New user created: " + createUserRequestJsonString);
        boolean isAdded = userRepository.tryAddUser(userIDCounter, user);
        if(isAdded) {
            CreateUserResponse response = new CreateUserResponse(userIDCounter);
            userIDCounter++;
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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