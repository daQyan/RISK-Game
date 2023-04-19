package ece651.RISC.Server.Model.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinGameRequest {
    private Long userId;
}