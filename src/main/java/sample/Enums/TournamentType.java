package sample.Enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public enum TournamentType {
    Swiss("Swiss"),
    RoundRobin("Round Robin"),
    DoubleRoundRobin("Double Round Robin"),
    HeadsUp("Heads up"),
    NotSpecified("Not specified");

    @Getter
    private String stringValue;

    public static TournamentType resolveFromString(String value) {
        for (TournamentType tournamentType : TournamentType.values()) {
            if (tournamentType.getStringValue().equals(value)) {
                return tournamentType;
            }
        }
        return NotSpecified;
    }

    public static ObservableList<String> getValues() {
        List<String> values = new LinkedList<>();

        for (TournamentType value : TournamentType.values()) {
            values.add(value.getStringValue());
        }

        return FXCollections.observableList(values);
    }
}
