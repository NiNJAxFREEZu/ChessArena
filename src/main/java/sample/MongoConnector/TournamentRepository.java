package sample.MongoConnector;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sample.Models.DAOs.TournamentDAO;

@Repository
public interface TournamentRepository extends MongoRepository<TournamentDAO, String> {
}
