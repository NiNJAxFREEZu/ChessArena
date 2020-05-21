package sample.Models.DTOs;

import lombok.*;
import sample.Models.DAOs.RoundDAO;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class RoundDTO {
    private Set<GameDTO> games;
    private Integer nr;

    public static RoundDTO map(RoundDAO roundDAO) {
        return RoundDTO.builder()
                .games(roundDAO
                        .getGames()
                        .stream()
                        .map(GameDTO::map)
                        .collect(Collectors.toSet())
                )
                .nr(roundDAO.getNr())
                .build();
    }
}
