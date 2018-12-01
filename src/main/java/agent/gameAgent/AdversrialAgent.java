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
        GameState gameState = null;
        return alphaBetaDecision(gameState);
    }

    private AgentAction alphaBetaDecision(GameState gameState){

        GameSearchOutput optimal = maxValue(gameState, Double.MIN_VALUE, Double.MAX_VALUE, getContext().getCutoff());
        return new AgentAction(optimal.getGameState().getCurrNode(), optimal.getGameState().getTime());
    }

    private GameSearchOutput maxValue(GameState gameState, double alpha, double beta, int timeToCutoff){
        if (gameState.isGoalState()){
            return new GameSearchOutput(gameState, gameState.getCostSoFar() - gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return new GameSearchOutput(gameState, Algorithm.heuristicStaticEvaluation(gameState));
        }
        double v = Double.MIN_VALUE;
        GameState maxState = null;
        List<GameState> expandState = expandState(gameState, true);
        for(GameState s: expandState){
            GameSearchOutput curr = minValue(s,alpha,beta,timeToCutoff-1);
            if(v < curr.getScore()){
                v = curr.getScore();
                maxState = curr.getGameState();
            }
            if (v >= beta){
                return  new GameSearchOutput(maxState, v);
            }
            alpha = Math.max(alpha,v);
        }
        return new GameSearchOutput(maxState, v);
    }

    private GameSearchOutput minValue(GameState gameState, double alpha, double beta, int timeToCutoff){
        if (gameState.isGoalState()){
            return new GameSearchOutput(gameState, gameState.getCostSoFar() - gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return new GameSearchOutput(gameState, Algorithm.heuristicStaticEvaluation(gameState));
        }
        Double v = Double.MAX_VALUE;
        GameState minState = null;
        List<GameState> expandState = expandState(gameState, false);
        for(GameState s: expandState){
            GameSearchOutput curr = maxValue(s,alpha,beta,timeToCutoff-1);
            if (v > curr.getScore()){
                v = curr.getScore();
                minState = curr.getGameState();
            }
            if (v <= alpha){
                return new GameSearchOutput(minState, v);
            }
            beta = Math.min(beta,v);
        }
        return new GameSearchOutput(minState, v);
    }


    private List<GameState> expandState(GameState gameState, boolean isA) {
        List<GameState> expandState = new LinkedList<>();
        HurricaneNode expandFrom;
        expandFrom = isA ? gameState.getCurrNode() : gameState.getOtherCurrNode();

        Iterator<Edge> it = expandFrom.getEdgeIterator();
        while (it.hasNext()) {
            Edge currentEdge = it.next();
            if (!HurricaneGraph.isEdgeBlock(currentEdge)) {
                HurricaneNode node = currentEdge.getOpposite(gameState.getCurrNode());
                expandState.add(constructNewState(gameState, currentEdge, node, isA));
            }
        }
        expandState.add(expandNoOp(gameState));
        return expandState;
    }



}
