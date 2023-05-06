package ece651.RISC.Server.Model.Payload.Response;

import lombok.Data;

@Data
public class CreateGameResponse {
    private long gameId;
    public CreateGameResponse(long gameId) {
        this.gameId = gameId;
    }
}