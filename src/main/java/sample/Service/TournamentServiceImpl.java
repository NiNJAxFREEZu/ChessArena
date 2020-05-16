package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DAOs.TournamentDAO;
import sample.MongoConnector.TournamentRepository;

import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public List<TournamentDAO> getTournaments() {
        List<TournamentDAO> all = tournamentRepository.findAll();
        return all;
    }
}
