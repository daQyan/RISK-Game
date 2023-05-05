package ece651.RISC.Server.Model.Payload.Request;

import lombok.Data;

@Data
public class FormAllyRequest {
    private int myPlayerId;
    private int targetPlayerId;
}
