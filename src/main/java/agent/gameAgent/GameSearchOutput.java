package agent.gameAgent;

import lombok.Data;
import states.GameState;

@Data
public class GameSearchOutput {

    public GameSearchOutput(GameState gameState, double score){
        this.gameState = gameState;
        this.score = score;
    }

    private GameState gameState;
    private double score;


}
