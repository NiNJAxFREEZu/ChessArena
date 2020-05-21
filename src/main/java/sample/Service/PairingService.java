package sample.Service;

import org.springframework.stereotype.Service;
import sample.Models.DTOs.PlayerDTO;
import sample.Models.DTOs.RoundDTO;

import java.util.List;

@Service
public interface PairingService {
    RoundDTO getPlayerPairing(List<PlayerDTO> players);

    RoundDTO getPlayerPairing(RoundDTO previousRound);

}
