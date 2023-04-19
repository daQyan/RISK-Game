package ece651.RISC.Server.Model.Payload.Request;

import lombok.Data;

@Data
public class CreateGameRequest {
    private int roomSize;
    private int initialUnit;
}
