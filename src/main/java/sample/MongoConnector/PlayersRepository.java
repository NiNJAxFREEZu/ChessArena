package sample.MongoConnector;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sample.Models.DAOs.PlayerDAO;
import sample.Models.DTOs.PlayerDTO;

import java.util.Optional;

@Repository
public interface PlayersRepository extends MongoRepository<PlayerDAO, String> {
    Optional<PlayerDAO> findByName(String name);

    Optional<PlayerDAO> findBySurname(String fullName);

    Optional<PlayerDAO> findByLicenseID(String licenseID);

    Optional<PlayerDAO> findByNameAndSurnameAndLicenseID(String name, String fullName, String licenseID);

    default Optional<PlayerDAO> findPlayerByDetails(PlayerDTO playerDTO) {
        return findByNameAndSurnameAndLicenseID(playerDTO.getName(), playerDTO.getSurname(), playerDTO.getLicenseID());
    }
}
