package agent.gameAgent;

import agent.Agent;
import config.HurricaneNode;
import lombok.Getter;
import lombok.Setter;
import org.graphstream.graph.Edge;
import simulator.SimulatorContext;
import states.AgentState;
import states.GameState;

import java.util.HashMap;
import java.util.Map;

public abstract class GameAgent extends Agent {

    public GameAgent(SimulatorContext context) {
        super(context);
    }

    @Override
    public void doActionInNode() {
        AgentState state = context.getAgentStates().get(this.getId());
        if (getCurrNode().isShelter()) {
            getCurrNode().setPeople(getPeople() + getCurrNode().getPeople());
            setPeople(0);
            state.setPeopleInCar(0);
        } else {
            setPeople(getPeople() + getCurrNode().getPeople());
            getCurrNode().setPeople(0);
            state.setPeopleInCar(getPeople());
        }

    }

    protected GameState constructNewState(GameState s, Edge currentEdge, HurricaneNode node, boolean isA) {
        if (isA){
            if (s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()) > context.getDeadline()){
                return new GameState(s, s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()));
            }
            Map<String, Integer> peopleMap = new HashMap<>(s.getPeopleInNodes());
            if (node.isShelter()) {
                peopleMap.put(node.getId(), node.getPeople() + s.getPeople());
            } else {
                peopleMap.put(node.getId(), 0);
            }
            return new GameState(s,
                    node,
                    peopleMap,
                    s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()),
                    node.isShelter() ? 0 : s.getPeople() + node.getPeople(),
                    s.getCostSoFar(),
                    node.isShelter() ? s.getMineSavedPeople() + s.getPeople() : s.getMineSavedPeople(),
                    s.getOtherCurrNode(),
                    s.getOtherSavedPeople(),
                    s.getOtherPeople());
        } else {
            if (s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getOtherPeople()) > context.getDeadline()){
                return new GameState(s, s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getOtherPeople()));
            }
            Map<String, Integer> peopleMap = new HashMap<>(s.getPeopleInNodes());
            if (node.isShelter()) {
                peopleMap.put(node.getId(), node.getPeople() + s.getOtherPeople());
            } else {
                peopleMap.put(node.getId(), 0);
            }
            return new GameState(s,
                    s.getCurrNode(),
                    peopleMap,
                    s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getOtherPeople()),
                    s.getPeople(),
                    s.getCostSoFar(),
                    s.getMineSavedPeople(),
                    node,
                    node.isShelter() ? s.getOtherSavedPeople() + s.getOtherPeople() : s.getOtherSavedPeople(),
                    node.isShelter() ? 0 : s.getOtherPeople() + node.getPeople());
        }
    }

    private double calculateTraverseSearchOperation(Edge e, int peopleInVehicle){
        return e.getNumber("weight") * (1 + context.getK() * peopleInVehicle);

    }

    protected GameState expandNoOp(GameState gameState) {
        return new GameState(gameState, gameState.getTime()+1);
    }
}
