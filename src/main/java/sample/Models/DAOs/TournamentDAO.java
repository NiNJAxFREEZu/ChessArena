package sample.Models.DAOs;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import sample.Enums.TournamentType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Tournaments")
@Builder
public class TournamentDAO {
    private List<PlayerDAO> playerList;
    private List<RoundDAO> rounds;
    private Integer roundNo;
    private TournamentType tournamentType;
    private Integer numberOfRounds;
}
