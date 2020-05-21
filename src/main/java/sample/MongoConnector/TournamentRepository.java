package sample.MongoConnector;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sample.Models.DAOs.TournamentDAO;
import sample.Models.DTOs.Tournament;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface TournamentRepository extends MongoRepository<TournamentDAO, String> {

    default List<Tournament> getTournaments() {
        return findAll()
                .stream()
                .map(Tournament::map)
                .collect(Collectors.toList());
    }
}
