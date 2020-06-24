package sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.Models.DTOs.GameDTO;
import sample.Models.DTOs.PlayerDTO;

@Service
public class RankingService {
    @Autowired
    private PlayerService playerService;

    void updatePlayerRatings(GameDTO game) {
        PlayerDTO whitePlayer = playerService.findPlayerByID(game.getPlayerWhiteID());
        PlayerDTO blackPlayer = playerService.findPlayerByID(game.getPlayerBlackID());
        double pWhite = getPlayerPropability(whitePlayer, blackPlayer), pBlack = getPlayerPropability(blackPlayer, whitePlayer);

        switch (game.getScore()) {
            case WhiteWon: {
                whitePlayer.setRating(getPlayerEloRating(whitePlayer, pWhite, 1.0));
                blackPlayer.setRating(getPlayerEloRating(blackPlayer, pBlack, 0));
            }

            case BlackWon: {
                whitePlayer.setRating(getPlayerEloRating(whitePlayer, pWhite, 0));
                blackPlayer.setRating(getPlayerEloRating(blackPlayer, pBlack, 1.0));
            }

            case Draw: {
                whitePlayer.setRating(getPlayerEloRating(whitePlayer, pWhite, 0.5));
                blackPlayer.setRating(getPlayerEloRating(blackPlayer, pBlack, 0.5));
            }

            case NotFinished: {
                return;
            }

            default: {
                return;
            }
        }


    }

    private double getPlayerPropability(PlayerDTO player, PlayerDTO opponent) {
        return 1.0 / (1.0 + Math.pow(10.0, (opponent.getRating() - player.getRating()) / 400.0));
    }

    private int getPlayerEloRating(PlayerDTO player, double probability, double score) {
        //Determinig K-factor
        int kFactor;

        if(player.getRating() < 2100)
            kFactor = 32;
        else if(player.getRating() >= 2100 && player.getRating() < 2400)
            kFactor = 24;
        else //player rating above 2400 elo
            kFactor = 16;

        return (int) (player.getRating() + kFactor * (score - probability));
    }
}
