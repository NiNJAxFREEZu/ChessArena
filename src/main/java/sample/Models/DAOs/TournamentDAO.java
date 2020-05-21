package sample.Models.DAOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sample.Enums.TournamentType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Tournaments")
@Builder
public class TournamentDAO {
    @Id
    private ObjectId _id;
    private String name;
    @Builder.Default
    private List<PlayerDAO> playerList = new ArrayList<>();
    @Builder.Default
    private List<RoundDAO> rounds = new ArrayList<>();
    private Integer roundNo;
    private String description;
    private Long date;
    @Builder.Default
    private TournamentType tournamentType = TournamentType.NotSpecified;
    private Integer numberOfRounds;
}
