package sample.Service;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Enums.Score;
import sample.Models.DTOs.*;
import sample.MongoConnector.TournamentRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private PairingService pairingService;
    @Autowired
    private RankingService rankingService;

    public static Tournament currentTournament;
    private static RoundDTO currentRound;
    private static RoundDTO previousRound;

    public static ObservableList<GameDTO> getCurrentGamesList() {
        return FXCollections.observableList(new ArrayList<>(currentRound.getGames()));
    }

    public static RoundDTO getCurrentRound() {
        return currentRound;
    }

    public static Integer getCurrentRoundNo() {
        return currentTournament.getRoundNo();
    }

    public void setScores() {
        Set<GameDTO> gamesPlayed = currentTournament
                .getRounds()
                .get(getCurrentRoundNo() - 1)
                .getGames();

        for (GameDTO gameDTO : gamesPlayed) {
            Score score = gameDTO.getScore();
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
    }

    public void finishTournament(List<GameDTO> modifiedGamesList) {
        currentTournament
                .getRounds()
                .get(getCurrentRoundNo() - 1)
                .setGames(new HashSet<>(modifiedGamesList));
        setScores();
    }

    private void addNPointsToUserById(Float points, String id) {
        List<PlayerDTO> playerList = currentTournament.getPlayerList();
        for (PlayerDTO playerDTO : playerList) {
            if (playerDTO.getPlayerID().equals(id)) {
                Float score = playerDTO.getScore();
                playerDTO.setScore(score + points);
            }
        }
    }

    public void startTournament() {
        currentTournament.incrementRound();
        currentRound = pairingService.getPlayerPairings();
        currentTournament.addRound(currentRound);
        previousRound = null;
    }

    public void nextRound(List<GameDTO> modifiedGamesList) {
        previousRound = currentRound;

        //Updating player ratings
        for (GameDTO game: modifiedGamesList) {
            rankingService.updatePlayerRatings(game);
        }

        currentTournament
                .getRounds()
                .get(getCurrentRoundNo() - 1)
                .setGames(new HashSet<>(modifiedGamesList));
        setScores();
        currentTournament.incrementRound();
        currentRound = pairingService.getPlayerPairings();
        currentTournament.addRound(currentRound);
    }

    public void createTournament(CreatingTournamentForm creatingTournamentForm, List<PlayerDTO> players) {
        if (Objects.isNull(creatingTournamentForm.getName())) {
            creatingTournamentForm.setName("Tournament");
        }

        Tournament tournament = Tournament.create(creatingTournamentForm);
        tournament.setPlayerList(players);
        currentTournament = tournament;
        currentRound = new RoundDTO();
    }

    /**
     * Ends tournament and creates and saves protocol of it.
     *
     * @param protocolOutputPath           - path to folder where protocol file will be saved
     * @param functionToInvokeIfFileExists - supply function that asks user if he wants to override file
     */
    @SneakyThrows
    public void endTournament(String protocolOutputPath, Supplier<Boolean> functionToInvokeIfFileExists) {
        saveProtocol(protocolOutputPath, functionToInvokeIfFileExists);
//        currentTournament.endTournament();
    }

    static RoundDTO getPlayerPairing(List<PlayerDTO> players) {
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

    /**
     * Saves current tournament to .json file with NAME in the folder PATH
     * [usage: saveToFile("C:/Penis", "penis1", () -> askForOverride());]
     *
     * @param fileName                     - NAME
     * @param folderPath                   - PATH
     * @param functionToInvokeIfFileExists - supply function that asks user if he wants to override file
     */
    public void saveToFile(String folderPath, String fileName, Supplier<Boolean> functionToInvokeIfFileExists) {
        Path path = resolveTournamentFilePath(folderPath, fileName);

        if (!checkIfCanCreateFile(path, functionToInvokeIfFileExists)) {
            return;
        }

        String tournamentJson = new Gson().toJson(currentTournament);
        try {
            File jsonFile = new File(path.toString());
            if (!jsonFile.exists()) {
                boolean createdDirs = jsonFile.getParentFile().mkdirs();
                boolean createdFile = jsonFile.createNewFile();
                if (!createdDirs && !createdFile) {
                    throw new Exception("Failed to create file and/or directory");
                }
            }
            Files.write(jsonFile.toPath(), Collections.singleton(tournamentJson));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path resolveTournamentFilePath(String folderPath, String fileName) {
        String defaultFileName;
        String defaultFolderName;

        if (fileName == null) {
            defaultFileName = currentTournament.getName()
                    .concat("_")
                    .concat(LocalDate.now().toString());
        } else {
            defaultFileName = fileName;
        }

        if (folderPath == null) {
            defaultFolderName = System.getProperty("user.home") + "/Desktop";
        } else {
            defaultFolderName = folderPath;
        }

        return Paths.get(defaultFolderName + "/" + defaultFileName + ".json").toAbsolutePath();
    }

    private Path resolveProtocolFilePath(String protocolFileOutputPath) {
        String protocolFileName = currentTournament.getName().concat(LocalDate.now().toString()).concat("_protocol");
        return Paths.get(protocolFileOutputPath + "/" + protocolFileName).toAbsolutePath();
    }

    @SneakyThrows
    private void saveProtocol(String protocolOutputPath, Supplier<Boolean> functionToInvokeIfFileExists) {
//        String protocol = currentTournament.createProtocol();
        Path protocolFilePath = resolveProtocolFilePath(protocolOutputPath);

        if (!checkIfCanCreateFile(protocolFilePath, functionToInvokeIfFileExists)) {
            return;
        }

        Path file = Files.createFile(protocolFilePath);
//        Files.write(file, Collections.singleton(protocol));
    }

    @SneakyThrows
    private boolean checkIfCanCreateFile(Path filePath, Supplier<Boolean> functionToInvokeIfFileExists) {
        if (Files.exists(filePath)) {
            if (functionToInvokeIfFileExists.get()) {
                Files.delete(filePath);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Opens .json containing tournament info.
     */
    @SneakyThrows
    public void openFromFile(String filePath) {
        String resolvedFilePath = System.getProperty("user.home") + "/Desktop";

        if (filePath.contains("/")) {
            resolvedFilePath = filePath;
        } else {
            resolvedFilePath = resolvedFilePath.concat("/").concat(filePath).concat(".json");
        }

        Path path = Paths.get(resolvedFilePath).toAbsolutePath();

        if (!Files.exists(path)) throw new RuntimeException("Such file doesn't exist!");

        String savedTournament = Files
                .readAllLines(path)
                .stream()
                .reduce(String::concat)
                .get();

        Gson tournamentGson = new Gson();
        currentTournament = tournamentGson.fromJson(savedTournament, Tournament.class);

        if (currentTournament.getRounds() != null && !currentTournament.getRounds().isEmpty()) {
            currentRound = currentTournament.getRounds()
                    .stream()
                    .max(Comparator.comparing(RoundDTO::getNr))
                    .get();
        } else {
            currentRound = null;
        }
    }
}
