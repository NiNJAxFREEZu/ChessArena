package sample.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Score {
    WhiteWon("White won"),
    Draw("Draw"),
    BlackWon("Black won"),
    NotFinished("Not finished");

    @Getter
    private String stringValue;

    public static Score resolveFromStringValue(String value) {
        for (Score score : Score.values()) {
            if (score.getStringValue().equals(value)) return score;
        }
        return null;
    }
}
