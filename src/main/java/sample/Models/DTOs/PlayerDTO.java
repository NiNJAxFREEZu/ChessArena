package sample.Models.DTOs;

import lombok.*;
import sample.Models.DAOs.PlayerDAO;
import sample.Service.ExternalSystemsService;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PlayerDTO {
    private String playerID;
    private String name;
    private String fullName;
    private String licenseID;
    private Float score;
    private Integer rating;

    public static PlayerDTO map(PlayerDAO playerDAO) {
        return PlayerDTO.builder()
                .playerID(playerDAO.get_id().toString())
                .name(playerDAO.getName())
                .fullName(playerDAO.getFullName())
                .licenseID(playerDAO.getLicenseID())
                .score(playerDAO.getScore())
                .rating(playerDAO.getRating())
                .build();
    }

    public static PlayerDTO map(CreatingPlayerForm creatingPlayerForm) {
        return PlayerDTO.builder()
                .playerID(creatingPlayerForm.getPlayerID().toString())
                .name(creatingPlayerForm.getName())
                .fullName(creatingPlayerForm.getFullName())
                .licenseID(creatingPlayerForm.getLicenseID())
                .score(creatingPlayerForm.getScore())
                .rating(ExternalSystemsService.getRankingOfThePlayer(creatingPlayerForm.getLicenseID()))
                .build();
    }
}
