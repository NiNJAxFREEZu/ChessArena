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
    private String surname;
    private String licenseID;
    private String title;
    private Float score;
    private Integer rating;
    private String gender;
    private String club;

    public static PlayerDTO map(PlayerDAO playerDAO) {
        return PlayerDTO.builder()
                .playerID(playerDAO.get_id().toString())
                .name(playerDAO.getName())
                .surname(playerDAO.getSurname())
                .licenseID(playerDAO.getLicenseID())
                .score(playerDAO.getScore())
                .rating(playerDAO.getRating())
                .gender(playerDAO.getGender())
                .club(playerDAO.getClub())
                .title(playerDAO.getTitle())
                .build();
    }

    public static PlayerDTO map(CreatingPlayerForm creatingPlayerForm) {
        return PlayerDTO.builder()
                .playerID(creatingPlayerForm.getPlayerID().toString())
                .name(creatingPlayerForm.getName())
                .surname(creatingPlayerForm.getSurname())
                .licenseID(creatingPlayerForm.getLicenseID())
                .score(creatingPlayerForm.getScore())
                .rating(ExternalSystemsService.getRankingOfThePlayer(creatingPlayerForm.getLicenseID()))
                .gender(creatingPlayerForm.getGender())
                .club(creatingPlayerForm.getClub())
                .title(creatingPlayerForm.getTitle())
                .build();
    }
}
