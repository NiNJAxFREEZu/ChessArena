package sample.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.Enums.Score;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultzInputForm {
    private Float whitePoints = null;
    private Float blackPoints = null;
    private boolean confirmed = false;

    public Score getScore() {
        if (whitePoints > blackPoints) {
            return Score.WhiteWon;
        } else if (whitePoints < blackPoints) {
            return Score.BlackWon;
        } else {
            return Score.Draw;
        }
    }
}
