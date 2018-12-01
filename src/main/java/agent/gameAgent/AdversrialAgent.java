package agent.gameAgent;

import agent.AgentAction;
import algorithms.Algorithm;
import config.HurricaneGraph;
import config.HurricaneNode;
import org.graphstream.graph.Edge;
import simulator.SimulatorContext;
import states.GameState;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AdversrialAgent extends GameAgent {

    public AdversrialAgent(SimulatorContext context){
        super(context);
    }

    @Override
    public AgentAction doNextAction(double currTime) {
        return null;
    }

    private AgentAction alphaBetaDecision(GameState gameState){
        GameState optimal = maxValue(gameState, Double.MIN_VALUE, Double.MAX_VALUE, getContext().getCutoff());
        return extractActionFromState(optimal);
    }

    private double maxValue(GameState gameState, double alpha, double beta, int timeToCutoff){
        if (gameState.isGoalState()){
            return gameState.getCostSoFar() - gameState.getOtherSavedPeople();
        }
        if (timeToCutoff == 0){
            return Algorithm.heuristicStaticEvaluation(gameState);
        }
        Double v = Double.MIN_VALUE;
        List<GameState> expandState = new LinkedList<>();
        Iterator<Edge> it = gameState.getCurrNode().getEdgeIterator();
        while (it.hasNext()) {
            Edge currentEdge = it.next();
            if (!HurricaneGraph.isEdgeBlock(currentEdge)) {
                HurricaneNode node = currentEdge.getOpposite(gameState.getCurrNode());
                expandState.add(constructNewState(gameState, currentEdge, node, true));
            }
        }
        expandState.add(expandNoOp(gameState));


        return 0.0;
    }


    private GameState minValue(GameState gameState, double alpha, double beta, int timeToCutoff){
        return null;
    }

    private AgentAction extractActionFromState(GameState gameState){
        return null;
    }


}
