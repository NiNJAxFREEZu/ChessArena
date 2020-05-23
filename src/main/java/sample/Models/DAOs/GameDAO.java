package sample.Models.DAOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import sample.Enums.Score;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameDAO {
    @Id
    private ObjectId _id;
    private String playerWhiteID;
    private String playerBlackID;
    private Integer chessboardNo;
    @Builder.Default
    private Score score = Score.NotFinished;
}

