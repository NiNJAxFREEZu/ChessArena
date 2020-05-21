package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Enums.Score;
import sample.Models.DTOs.*;
import sample.MongoConnector.TournamentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private static RoundDTO currentRound;
    private static RoundDTO previousRound;
    private static TournamentInProgress currentTournament;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private PairingService pairingService;

    public static RoundDTO getCurrentRound() {
        return currentRound;
    }

    public void setScores(Score score, Integer chessboardNo) {
        GameDTO gameDTO = currentRound.getGames().stream().filter((e) -> e.getChessboardNo().equals(chessboardNo)).findFirst().get();
        gameDTO.setScore(score);

        switch (score) {
            case Draw:
                addNPointsToUserById(0.5f, gameDTO.getPlayerBlackID());
                addNPointsToUserById(0.5f, gameDTO.getPlayerWhiteID());
                break;
            case BlackWon:
                addNPointsToUserById(1f, gameDTO.getPlayerBlackID());
                break;
            case WhiteWon:
                addNPointsToUserById(1f, gameDTO.getPlayerWhiteID());
                break;
        }
    }

    private void addNPointsToUserById(Float points, String id) {
        List<PlayerDTO> updatedPlayerList = currentTournament
                .getPlayerList()
                .stream()
                .filter((e) -> e.getPlayerID().equals(id))
                .peek((e) -> e.setScore(e.getScore() + points))
                .collect(Collectors.toList());

        currentTournament.setPlayerList(updatedPlayerList);
    }

    public void startTournament(CreatingTournamentForm creatingTournamentForm, List<PlayerDTO> players) {
        currentTournament = new TournamentInProgress(Tournament.create(creatingTournamentForm));
        currentRound = pairingService.getPlayerPairing(players);
        previousRound = null;
    }

    public void nextRound() {
        previousRound = currentRound;
        currentRound = pairingService.getPlayerPairing(previousRound);
    }
}
