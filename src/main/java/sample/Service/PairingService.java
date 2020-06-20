package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.RoundDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class PairingService {
    @Autowired
    private PlayerService playerService;
    private HashMap<String, HashSet<String>> pairingHistory;

    RoundDTO getPlayerPairings(List<PlayerDTO> players) {
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

    //VERY importanet methoed
    RoundDTO getPlayerPairings() {
        return null;
    }

    private void buildPlayersPairingHistory() {
        HashMap<String, HashSet<String>> pairingHistory = new HashMap<>();

        //Adding players to hashmap
        for(PlayerDTO player : playerService.getAllPlayers()) {
            pairingHistory.put(player.getPlayerID(), new HashSet<>());
        }

        //Building hashsets
        for (RoundDTO round : TournamentService.currentTournament.getRounds()) {
            for(GameDTO game : round.getGames()) {
                pairingHistory.get(game.getPlayerWhiteID()).add(game.getPlayerBlackID());
                pairingHistory.get(game.getPlayerBlackID()).add(game.getPlayerWhiteID());
            }
        }
        this.pairingHistory = pairingHistory;
    }

    private boolean playedTogether(PlayerDTO player1, PlayerDTO player2) {
        for(String player2ID : pairingHistory.get(player1.getPlayerID())) {
            if(player2.getPlayerID().equals(player2ID))
                return true;
        }
        return false;
    }

}
