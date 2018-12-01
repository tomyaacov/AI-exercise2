package states;

import config.HurricaneNode;
import lombok.Getter;
import lombok.Setter;

public class AgentState {

    public AgentState(int savedPeople){
        this.savedPeople = savedPeople;
    }

    @Getter @Setter
    private int savedPeople;

    @Getter @Setter
    private int peopleInCar;

    @Getter @Setter
    private HurricaneNode position;
}
