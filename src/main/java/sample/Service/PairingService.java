package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.RoundDTO;

import java.util.HashSet;
import java.util.List;

@Service
public class PairingService {
    @Autowired
    private PlayerService playerService;

    RoundDTO getPlayerPairing(List<PlayerDTO> players) {
        GameDTO game1 = GameDTO.builder()
                .chessboardNo(1)
                .playerBlackID("playerBlackID1")
                .playerWhiteID("playerWhiteID1")
                .playerBlackShortName(players.get(0).getShortName())
                .playerWhiteShortName(players.get(1).getShortName())
                .build();

        GameDTO game2 = GameDTO.builder()
                .chessboardNo(2)
                .playerBlackID("playerBlackID2")
                .playerWhiteID("playerWhiteID2")
                .playerBlackShortName(players.get(0).getShortName())
                .playerWhiteShortName(players.get(1).getShortName())
                .build();
        HashSet<GameDTO> set = new HashSet<>();
        set.add(game1);
        set.add(game2);

        return RoundDTO.builder()
                .nr(1)
                .games(set)
                .build();
    }

    RoundDTO getPlayerPairing(RoundDTO previousRound) {
        return null;
    }

}
