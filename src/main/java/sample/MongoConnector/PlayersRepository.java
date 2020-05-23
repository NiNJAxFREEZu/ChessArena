package sample.MongoConnector;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sample.Models.DAOs.PlayerDAO;
import sample.Models.DTOs.PlayerDTO;

import java.util.Optional;

@Repository
public interface PlayersRepository extends MongoRepository<PlayerDAO, String> {
    Optional<PlayerDAO> findByName(String name);

    Optional<PlayerDAO> findByFullName(String fullName);

    Optional<PlayerDAO> findByLicenseID(String licenseID);

    Optional<PlayerDAO> findByNameAndFullNameAndLicenseID(String name, String fullName, String licenseID);

    default Optional<PlayerDAO> findPlayerByDetails(PlayerDTO playerDTO) {
        return findByNameAndFullNameAndLicenseID(playerDTO.getName(), playerDTO.getFullName(), playerDTO.getLicenseID());
    }
}
