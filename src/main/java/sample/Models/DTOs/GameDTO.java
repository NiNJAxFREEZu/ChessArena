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
    private String playerWhiteShortName;
    private String playerBlackShortName;
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

    public static GameDTO create(Integer chessboardNo, PlayerDTO... players) {
        return GameDTO.builder()
                .playerWhiteID(players[0].getPlayerID())
                .playerBlackID(players[1].getPlayerID())
                .playerWhiteShortName(players[0].getShortName())
                .playerBlackShortName(players[1].getShortName())
                .chessboardNo(chessboardNo)
                .build();
    }
}
