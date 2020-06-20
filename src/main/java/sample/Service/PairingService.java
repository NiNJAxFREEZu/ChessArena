package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.RoundDTO;

import java.util.*;
import java.util.stream.Collectors;

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

    /*
    VERY importanet methoed - for now let's assume that player count is EVEN
    RULE 0 -> pair players for first round according to their ratings
    RULE 1 -> pair players according to their tournament round history (two players cannot play together twice)
    */
    RoundDTO getPlayerPairings() {
        //First round
        if(TournamentService.currentTournament.getRoundNo() == 1)
            return getFirstRoundPairings();

        else {
            switch (TournamentService.currentTournament.getTournamentType()) {
                case RoundRobin:
                    return getRoundRobinPairings();

                case DoubleRoundRobin:
                    return null;

                case HeadsUp:
                    return null;

                case Swiss:
                    return getSwissPairings();

                default:
                    return null;
            }
        }
    }

    private RoundDTO getFirstRoundPairings() {
        List<PlayerDTO> playersToPair = new LinkedList<>();
        Collections.copy(playersToPair, TournamentService.currentTournament.getPlayerList());
        Set<GameDTO> firstRoundGames = new HashSet<>();
        int chessBoardNo = 1;

        playersToPair = playersToPair.stream()
                .sorted(Comparator.comparing(PlayerDTO::getRating))
                .collect(Collectors.toList());

        while (playersToPair.size() > 0) {
            if(playersToPair.size() >= 2) {
                PlayerDTO player1 = playersToPair.get(0), player2 = playersToPair.get(1);
                firstRoundGames.add(GameDTO.create(chessBoardNo, player1, player2));
                playersToPair.remove(player1);
                playersToPair.remove(player2);
            }
            //One player in list remaining
            else{
                break; //TODO
            }

            ++chessBoardNo;
        }

        return RoundDTO.builder().games(firstRoundGames).build();
    }

    private RoundDTO getSwissPairings() {
        return null;
    }

    private RoundDTO getRoundRobinPairings() {
        buildPlayersPairingHistory();
        List<PlayerDTO> playersToPair = new LinkedList<>();
        Set<GameDTO> newRoundGames = new HashSet<>();
        Collections.copy(playersToPair, TournamentService.currentTournament.getPlayerList());

        int chessBoardNo = 1;
        while (playersToPair.size() > 0) {
            if(playersToPair.size() >=2) {
                int playerListIndex = 0;
                PlayerDTO player1 = playersToPair.get(playerListIndex), player2;

                //Looking for an opponent for player 1
                do {
                    player2 = playersToPair.get(++playerListIndex);
                }
                while (!playedTogether(player1, player2));

                newRoundGames.add(GameDTO.create(chessBoardNo, player1, player2));
                playersToPair.remove(player1);
                playersToPair.remove(player2);
            }
            //One player in list remaining
            else {
                break; //TODO
            }

            chessBoardNo++;
        }
        return RoundDTO.builder().games(newRoundGames).build();
    }


    private void buildPlayersPairingHistory() {
        HashMap<String, HashSet<String>> pairingHistory = new HashMap<>();

        //Adding players to hashmap
        for(PlayerDTO player : TournamentService.currentTournament.getPlayerList()) {
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
