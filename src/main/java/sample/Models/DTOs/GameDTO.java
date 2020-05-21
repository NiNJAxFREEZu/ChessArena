package sample.Models.DTOs;

import lombok.*;
import sample.Enums.Score;
import sample.Models.DAOs.GameDAO;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class GameDTO {
    private String playerWhiteID;
    private String playerBlackID;
    private Integer chessboardNo;
    @Builder.Default
    private Score score = Score.NotFinished;

    public static GameDTO map(GameDAO gameDAO) {
        return GameDTO.builder()
                .playerBlackID(gameDAO.getPlayerBlackID())
                .playerWhiteID(gameDAO.getPlayerWhiteID())
                .chessboardNo(gameDAO.getChessboardNo())
                .score(gameDAO.getScore())
                .build();
    }
}
