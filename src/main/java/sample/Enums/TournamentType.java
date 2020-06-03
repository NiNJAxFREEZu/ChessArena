package sample.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TournamentType {
    Swiss("Swiss"),
    RoundRobin("Round Robin"),
    DoubleRoundRobin("Double Round Robin"),
    HeadsUp("Heads up"),
    NotSpecified("Not specified");

    @Getter
    private String stringValue;
}
