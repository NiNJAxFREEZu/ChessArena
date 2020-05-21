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
@Builder
public class Tournament {
    private String name;
    private List<PlayerDTO> playerList;
    private List<RoundDTO> rounds;
    private Integer roundNo;
    private TournamentType tournamentType;
    private Integer numberOfRounds;
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
                .build();
    }
}
