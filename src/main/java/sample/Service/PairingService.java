package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Enums.Score;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.RoundDTO;
import sample.Service.exceptions.HaveToEndTournamentException;

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
        if (TournamentService.currentTournament.getRoundNo() == 1)
            return getFirstRoundPairings();

        else {
            switch (TournamentService.currentTournament.getTournamentType()) {
                case RoundRobin:
                    return getRoundRobinPairings();

                case DoubleRoundRobin:
                    return getDoubleRoundRobinPairings();

                case HeadsUp:
                    return getHeadsUpPairing();

                case Swiss:
                    return getSwissPairings();

                default:
                    return null;
            }
        }
    }

    //Done!
    private RoundDTO getFirstRoundPairings() {
        List<PlayerDTO> playersToPair = new LinkedList<PlayerDTO>(TournamentService.currentTournament.getPlayerList());
        Set<GameDTO> firstRoundGames = new HashSet<>();
        int chessBoardNo = 1;

        playersToPair = playersToPair.stream()
                .sorted(Comparator.comparing(PlayerDTO::getRating))
                .collect(Collectors.toList());

        while (playersToPair.size() > 0) {
            PlayerDTO player1 = playersToPair.get(0);

            if (playersToPair.size() >= 2) {
                PlayerDTO player2 = playersToPair.get(1);

                firstRoundGames.add(
                        GameDTO.create(chessBoardNo, player1, player2)
                );

                playersToPair.remove(player1);
                playersToPair.remove(player2);
            }
            //One player in list remaining
            else {
                //One remaining player will pause this round and will receive a point (White won)
                firstRoundGames.add(GameDTO.create(player1, Score.WhiteWon));
                playersToPair.remove(player1);
            }

            ++chessBoardNo;
        }

        return RoundDTO.builder()
                .nr(1)
                .games(firstRoundGames)
                .build();
    }

    //Testing!
    private RoundDTO getSwissPairings() {
        List<PlayerDTO> playersToPair = new LinkedList<PlayerDTO>(TournamentService.currentTournament.getPlayerList());
        Set<GameDTO> nextRoundGames = new HashSet<>();
        int chessBoardNo = 1;

        playersToPair = playersToPair.stream()
                .sorted(Comparator.comparing(PlayerDTO::getScore))
                .collect(Collectors.toList());

        while (playersToPair.size() > 0) {
            PlayerDTO player1 = playersToPair.get(0);

            if (playersToPair.size() >= 2) {
                PlayerDTO player2 = playersToPair.get(1);

                nextRoundGames.add(
                        GameDTO.create(chessBoardNo, player1, player2)
                );

                playersToPair.remove(player1);
                playersToPair.remove(player2);
            }
            //One player in list remaining
            else {
                //One remaining player will pause this round and will receive a point (White won)
                nextRoundGames.add(GameDTO.create(player1, Score.WhiteWon));
                playersToPair.remove(player1);
            }

            ++chessBoardNo;
        }

        return RoundDTO.builder()
                .nr(1)
                .games(nextRoundGames)
                .build();
    }

    //Done!
    private RoundDTO getRoundRobinPairings() {
        buildPlayersPairingHistory();
        List<PlayerDTO> playersToPair = new LinkedList<>(TournamentService.currentTournament.getPlayerList());
        Set<GameDTO> newRoundGames = new HashSet<>();

        playersToPair = playersToPair.stream()
                .sorted(Comparator.comparing(PlayerDTO::getScore))
                .collect(Collectors.toList());

        int chessBoardNo = 1;
        while (playersToPair.size() > 0) {
            int playerListIndex = 0;
            PlayerDTO player1 = playersToPair.get(playerListIndex), player2;

            if (playersToPair.size() >= 2) {

                //Looking for an opponent for player 1
                try {
                    do {
                        player2 = playersToPair.get(++playerListIndex);
                    }
                    while (playedTogether(player1, player2));
                }
                //Unable to pair players -> end of tournament
                catch (IndexOutOfBoundsException e) {
                    throw new HaveToEndTournamentException();
                }
                newRoundGames.add(GameDTO.create(chessBoardNo, player1, player2));
                playersToPair.remove(player1);
                playersToPair.remove(player2);
            }
            //One player in list remaining
            else {
                //One remaining player will pause this round and will receive a point (White won)
                newRoundGames.add(GameDTO.create(player1, Score.WhiteWon));
                playersToPair.remove(player1);
            }
            chessBoardNo++;
        }

        return RoundDTO.builder()
                .nr(TournamentService.getCurrentRoundNo())
                .games(newRoundGames)
                .build();
    }

    //TBD
    private RoundDTO getDoubleRoundRobinPairings() {
        return null;
    }

    //TBD
    private RoundDTO getHeadsUpPairing() {
        return null;
    }

    //Done!
    private void buildPlayersPairingHistory() {
        HashMap<String, HashSet<String>> pairingHistory = new HashMap<>();

        //Adding players to hashmap
        for (PlayerDTO player : TournamentService.currentTournament.getPlayerList()) {
            pairingHistory.put(player.getPlayerID(), new HashSet<>());
        }

        //Building hashsets
        for (RoundDTO round : TournamentService.currentTournament.getRounds()) {
            for (GameDTO game : round.getGames()) {
                pairingHistory.get(game.getPlayerWhiteID()).add(game.getPlayerBlackID());
                pairingHistory.get(game.getPlayerBlackID()).add(game.getPlayerWhiteID());
            }
        }
        this.pairingHistory = pairingHistory;
    }

    //Done!
    private boolean playedTogether(PlayerDTO player1, PlayerDTO player2) {
        return pairingHistory.get(player1.getPlayerID()).contains(player2.getPlayerID())
                || pairingHistory.get(player2.getPlayerID()).contains(player1.getPlayerID());
    }

}
