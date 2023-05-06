package ece651.RISC.Server.Model.Payload.Response;

import ece651.RISC.Server.Model.GameInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GetGameInfoResponse {
    private GameInfo gameInfo;
}
