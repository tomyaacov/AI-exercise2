package states;

import config.HurricaneNode;
import lombok.Getter;
import lombok.Setter;
import simulator.SimulatorContext;

import java.util.Map;

public class GameState extends State {

    @Getter @Setter
    private double mineSavedPeople;

    @Getter @Setter
    private HurricaneNode otherCurrNode;

    @Getter @Setter
    private double otherSavedPeople;

    @Getter @Setter
    private int otherPeople;

    public GameState(SimulatorContext simulatorContext, HurricaneNode currNode, State prev, int people, double costSoFar, double savedPeople, HurricaneNode otherCurrNode, double otherSavedPeople, int otherPeople) {
        super(simulatorContext, currNode, prev, people, costSoFar);
        this.mineSavedPeople = savedPeople;
        this.otherCurrNode = otherCurrNode;
        this.otherSavedPeople = otherSavedPeople;
        this.otherPeople = otherPeople;
    }

    public GameState(State prev,
                     HurricaneNode currNode,
                     Map<String, Integer> peopleInside,
                     double time,
                     int people,
                     double costSoFar,
                     double savedPeople,
                     HurricaneNode otherCurrNode,
                     double otherSavedPeople,
                     int otherPeople) {
        super(prev, currNode, peopleInside, time, people, costSoFar);
        this.mineSavedPeople = savedPeople;
        this.otherCurrNode = otherCurrNode;
        this.otherSavedPeople = otherSavedPeople;
        this.otherPeople = otherPeople;
    }

    public GameState(GameState gameState, double newTime){
        this.prev = gameState;
        this.currNode = gameState.getCurrNode();
        this.peopleInNodes = gameState.getPeopleInNodes();
        this.time = newTime;
        this.people = gameState.getPeople();
        this.costSoFar = gameState.getCostSoFar();
        this.otherCurrNode = gameState.getOtherCurrNode();
        this.otherSavedPeople = gameState.getOtherSavedPeople();
        this.otherPeople = gameState.getOtherPeople();
    }


}
