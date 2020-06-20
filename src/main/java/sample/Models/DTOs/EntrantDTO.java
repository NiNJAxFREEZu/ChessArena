package sample.Models.DTOs;

import lombok.*;
import sample.Models.DAOs.PlayerDAO;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class EntrantDTO {
    private String playerID;
    private String name;
    private String surname;
    private String licenseID;
    private String title;
    private Integer rating;
    private String gender;
    private String club;

    public static EntrantDTO map(PlayerDTO player) {
        return EntrantDTO.builder()
                .playerID(player.getPlayerID())
                .name(player.getName())
                .surname(player.getSurname())
                .licenseID(player.getLicenseID())
                .rating(player.getRating())
                .gender(player.getGender())
                .club(player.getClub())
                .title(player.getTitle())
                .build();
    }

    public static EntrantDTO map(PlayerDAO player) {
        return EntrantDTO.builder()
                .playerID(player.get_id().toString())
                .name(player.getName())
                .surname(player.getSurname())
                .licenseID(player.getLicenseID())
                .rating(player.getRating())
                .gender(player.getGender())
                .club(player.getClub())
                .title(player.getTitle())
                .build();
    }
}
