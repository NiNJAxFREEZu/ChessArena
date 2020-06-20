package sample.Enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

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
        return NotFinished;
    }

    public static ObservableList<String> getValues() {
        List<String> values = new LinkedList<>();

        for (Score value : Score.values()) {
            values.add(value.getStringValue());
        }

        return FXCollections.observableList(values);
    }
}
