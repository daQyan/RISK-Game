package ece651.RISC.Server.Model.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginUserResponse {
    private long user_id;
    private String user_name;

    private boolean isLoggedIn;
}