package sample.Service.exceptions;

public class HaveToEndTournamentException extends RuntimeException {
    public HaveToEndTournamentException() {
    }

    public HaveToEndTournamentException(String message) {
        super(message);
    }
}
