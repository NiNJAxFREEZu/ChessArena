package sample.Models.DAOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sample.Enums.Score;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Games")
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

