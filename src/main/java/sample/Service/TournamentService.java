package sample.Service;

import com.google.gson.Gson;
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
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private PairingService pairingService;

    public static Tournament currentTournament;
    private static RoundDTO currentRound;
    private static RoundDTO previousRound;

    public static RoundDTO getCurrentRound() {
        return currentRound;
    }

    public void setScores(Score score, Integer chessboardNo) {
        GameDTO gameDTO = currentRound.getGames()
                .stream()
                .filter((e) -> e.getChessboardNo().equals(chessboardNo))
                .findFirst()
                .get();
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
        currentTournament = Tournament.create(creatingTournamentForm, players);
        currentRound = pairingService.getPlayerPairings(players);
        currentTournament.getRounds().add(currentRound);
        previousRound = null;
    }

    public void nextRound() {
        previousRound = currentRound;
        currentRound = pairingService.getPlayerPairings();
        currentTournament.getRounds().add(currentRound);
    }

    public void createTournament(CreatingTournamentForm creatingTournamentForm, List<PlayerDTO> players) {
        Tournament tournament = Tournament.create(creatingTournamentForm);
        tournament.setPlayerList(players);
        currentTournament = tournament;
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

    /**
     * Opens .json containing tournament info.
     */
    @SneakyThrows
    public void openFromFile(String filePath) {
        String resolvedFilePath = System.getProperty("user.home") + "/Desktop";

        if (!filePath.contains("/")) {
            resolvedFilePath = filePath;
        } else {
            resolvedFilePath = resolvedFilePath.concat("/").concat(filePath);
        }

        Path path = Paths.get(resolvedFilePath);

        if (!Files.exists(path)) throw new RuntimeException("Such file doesn't exist!");

        String savedTournament = Files
                .readAllLines(path)
                .stream()
                .reduce(String::concat)
                .get();

        Gson tournamentGson = new Gson();
        currentTournament = tournamentGson.fromJson(savedTournament, Tournament.class);
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
}
