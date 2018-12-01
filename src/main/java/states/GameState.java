package states;

import config.HurricaneNode;
import lombok.Getter;
import lombok.Setter;
import simulator.SimulatorContext;

import java.util.Map;

public class GameState extends State {

    @Getter @Setter
    private HurricaneNode otherCurrNode;

    @Getter @Setter
    private double otherCostSoFar;

    @Getter @Setter
    private int otherPeople;


    public GameState(State prev, HurricaneNode currNode, Map<String, Integer> peopleInside, double time, int people, double costSoFar, HurricaneNode otherCurrNode, double otherCostSoFar, int otherPeople) {
        super(prev, currNode, peopleInside, time, people, costSoFar);
        this.otherCurrNode = otherCurrNode;
        this.otherCostSoFar = otherCostSoFar;
        this.otherPeople = otherPeople;
    }

    public GameState(SimulatorContext simulatorContext, HurricaneNode currNode, State prev, int people, double costSoFar, HurricaneNode otherCurrNode, double otherCostSoFar, int otherPeople) {
        super(simulatorContext, currNode, prev, people, costSoFar);
        this.otherCurrNode = otherCurrNode;
        this.otherCostSoFar = otherCostSoFar;
        this.otherPeople = otherPeople;
    }
}
