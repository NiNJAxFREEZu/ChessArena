package sample.Models.DTOs;

import lombok.*;
import sample.Models.DAOs.PlayerDAO;

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
}
