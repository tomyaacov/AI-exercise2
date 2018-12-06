package agent.gameAgent;

import lombok.Data;
import states.GameState;

@Data
public class GameSearchOutputSemi {

    public GameSearchOutputSemi(GameState gameState, double aScore, double bScore){
        this.gameState = gameState;
        this.aScore = aScore;
        this.bScore = bScore;
    }

    private GameState gameState;
    private double aScore;
    private double bScore;


}
