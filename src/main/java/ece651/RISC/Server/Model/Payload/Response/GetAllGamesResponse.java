package ece651.RISC.Server.Model.Payload.Response;

import ece651.RISC.Server.Model.GameInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GetAllGamesResponse {
    private List<GameInfo> games;
}