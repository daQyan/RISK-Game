package ece651.RISC.Server.Model.Payload.Request;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String username;
    private String password;
}