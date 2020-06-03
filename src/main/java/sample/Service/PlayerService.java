package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DAOs.PlayerDAO;
import sample.Models.DTOs.CreatingPlayerForm;
import sample.Models.DTOs.PlayerDTO;
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
}
