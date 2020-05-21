package sample.Models.DTOs;

import lombok.*;
import sample.Enums.TournamentType;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatingTournamentForm {
    private String name;
    private Long date;
    private TournamentType tournamentType;
}
