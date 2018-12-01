package agent.gameAgent;

import agent.Agent;
import config.HurricaneNode;
import org.graphstream.graph.Edge;
import simulator.SimulatorContext;
import states.GameState;
import states.State;

import java.util.HashMap;
import java.util.Map;

public abstract class GameAgent extends Agent {
    public GameAgent(SimulatorContext context) {
        super(context);
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
            return null;
        }
        return null;
    }

    private double calculateTraverseSearchOperation(Edge e, int peopleInVehicle){
        return e.getNumber("weight") * (1 + context.getK() * peopleInVehicle);

    }

    protected GameState expandNoOp(GameState gameState) {
        return new GameState(gameState, gameState.getTime()+1);
    }
}
