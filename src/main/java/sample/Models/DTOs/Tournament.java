package sample.Models.DTOs;

import lombok.*;
import sample.Enums.TournamentType;
import sample.Models.DAOs.TournamentDAO;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class Tournament implements TournamentInProgress {
    private String name;
    private List<PlayerDTO> playerList;
    private List<RoundDTO> rounds;
    private Integer roundNo;
    private Integer numberOfRounds;
    private TournamentType tournamentType;
    private Date date;

    public static Tournament map(TournamentDAO tournamentDAO) {
        return Tournament.builder()
                .name(tournamentDAO.getName())
                .playerList(tournamentDAO
                        .getPlayerList()
                        .stream()
                        .map(PlayerDTO::map)
                        .collect(Collectors.toList())
                )
                .rounds(tournamentDAO
                        .getRounds()
                        .stream()
                        .map(RoundDTO::map)
                        .collect(Collectors.toList()))
                .roundNo(tournamentDAO.getRoundNo())
                .tournamentType(tournamentDAO.getTournamentType())
                .numberOfRounds(tournamentDAO.getNumberOfRounds())
                .build();
    }

    public static Tournament create(CreatingTournamentForm creatingTournamentForm) {
        return Tournament.builder()
                .name(creatingTournamentForm.getName())
                .date(Date.from(Instant.ofEpochMilli(creatingTournamentForm.getDate())))
                .tournamentType(creatingTournamentForm.getTournamentType())
                .playerList(Collections.emptyList())
                .numberOfRounds(creatingTournamentForm.getAmountOfRounds())
                .build();
    }

    public static Tournament create(CreatingTournamentForm creatingTournamentForm, List<PlayerDTO> playerList) {
        return Tournament.builder()
                .name(creatingTournamentForm.getName())
                .date(Date.from(Instant.ofEpochMilli(creatingTournamentForm.getDate())))
                .tournamentType(creatingTournamentForm.getTournamentType())
                .playerList(playerList)
                .numberOfRounds(creatingTournamentForm.getAmountOfRounds())
                .build();
    }
}
