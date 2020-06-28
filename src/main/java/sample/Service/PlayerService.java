package sample.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DAOs.PlayerDAO;
import sample.Models.DTOs.CreatingPlayerForm;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.Tournament;
import sample.MongoConnector.PlayersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayersRepository playersRepository;

    public void createPlayer(CreatingPlayerForm creatingPlayerForm) {
        PlayerDTO playerDTO = PlayerDTO.map(creatingPlayerForm);

        playersRepository.findPlayerByDetails(playerDTO)
                .ifPresent((e) -> {
                    throw new RuntimeException("Player already exists: " + e.getSurname() + ", " + e.getName());
                });

        playersRepository.save(PlayerDAO.map(playerDTO));
    }

    public List<PlayerDTO> getAllPlayers() {
        return playersRepository.findAll().stream().map(PlayerDTO::map).collect(Collectors.toList());
    }

    public void updatePlayer(PlayerDTO playerDTO) {
        playersRepository.save(PlayerDAO.map(playerDTO));
    }

    public ObservableList<PlayerDTO> getAllEntrants() {
        List<PlayerDTO> collect = playersRepository.findAll().stream().map(PlayerDTO::map).collect(Collectors.toList());

        return FXCollections.observableList(collect);
    }

    public PlayerDTO findPlayerByID(String playerID) {
        return PlayerDTO.map(
                playersRepository
                        .findById(playerID)
                        .orElseThrow(RuntimeException::new)
        );
    }

    public void assignShortNames(List<GameDTO> games, Tournament tournament) {
        for (GameDTO game : games) {
            String playerBlackID = game.getPlayerBlackID();
            String playerWhiteID = game.getPlayerWhiteID();

            PlayerDTO blackPlayer = tournament.getPlayerList()
                    .stream()
                    .filter(e -> e.getPlayerID().equals(playerBlackID))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            PlayerDTO whitePlayer = tournament.getPlayerList()
                    .stream()
                    .filter(e -> e.getPlayerID().equals(playerWhiteID))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            game.setPlayerWhiteShortName(whitePlayer.getShortName());
            game.setPlayerBlackShortName(blackPlayer.getShortName());
        }
    }
}
