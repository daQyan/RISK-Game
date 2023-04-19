package ece651.RISC.Server.Model.Payload.Response;

import lombok.Data;

@Data
public class LoginUserResponse {
        private long userId;

        public LoginUserResponse(long userId) {
            this.userId = userId;
        }
    }