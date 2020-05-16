package sample.Service;

import org.springframework.stereotype.Service;
import sample.Models.DAOs.TournamentDAO;

import java.util.List;

@Service
public interface TournamentService {
    List<TournamentDAO> getTournaments();
}
