package sample.Models.DAOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sample.Models.DTOs.PlayerDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Players")
@Builder
public class PlayerDAO {
    @Id
    private ObjectId _id;
    private String name;
    private String fullName;
    private String licenseID;
    private Float score;
    private Integer rating;

    public static PlayerDAO map(PlayerDTO playerDAO) {
        return PlayerDAO.builder()
                ._id(new ObjectId(playerDAO.getPlayerID()))
                .name(playerDAO.getName())
                .fullName(playerDAO.getFullName())
                .licenseID(playerDAO.getLicenseID())
                .score(playerDAO.getScore())
                .rating(playerDAO.getRating())
                .build();
    }
}
