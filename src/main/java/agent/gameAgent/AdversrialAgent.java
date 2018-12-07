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
        int otherId = (getId()+1)%2;
        GameState gameState = new GameState(getContext(), getCurrNode(), null, getPeople(), 0,
                                            getContext().getAgentStates().get(getId()).getSavedPeople(),
                                            getContext().getAgentStates().get(otherId).getPosition(),
                                            getContext().getAgentStates().get(otherId).getSavedPeople(),
                                            getContext().getAgentStates().get(otherId).getPeopleInCar());
        AgentAction action = alphaBetaDecision(gameState);
        setCurrNode(action.getCurrNode());

        return action;
    }

    private AgentAction alphaBetaDecision(GameState gameState){

        GameSearchOutput optimal = maxValue(gameState, -Double.MAX_VALUE, Double.MAX_VALUE, getContext().getCutoff());
        context.getAgentStates().get(getId()).setPeopleInCar(optimal.getGameState().getPeople());
        context.getAgentStates().get(getId()).setPosition(optimal.getGameState().getCurrNode());
        context.getAgentStates().get(getId()).setSavedPeople(optimal.getGameState().getMineSavedPeople());


        return new AgentAction(optimal.getGameState().getCurrNode(), optimal.getGameState().getTime()- optimal.getGameState().getPrev().getTime());
    }

    private GameSearchOutput maxValue(GameState gameState, double alpha, double beta, int timeToCutoff){
        if (gameState.isGoalState()){
            return new GameSearchOutput(gameState, gameState.getMineSavedPeople() - gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return new GameSearchOutput(gameState, Algorithm.heuristicStaticEvaluation(gameState, context));
        }
        double v = -Double.MAX_VALUE;
        GameState maxState = null;
        List<GameState> expandState = expandState(gameState, true);
        for(GameState s: expandState){
            GameSearchOutput curr = minValue(s,alpha,beta,timeToCutoff-1);
            if(v < curr.getScore()){
                v = curr.getScore();
                maxState = s;
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
            return new GameSearchOutput(gameState, gameState.getMineSavedPeople() - gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return new GameSearchOutput(gameState, Algorithm.heuristicStaticEvaluation(gameState,context));
        }
        Double v = Double.MAX_VALUE;
        GameState minState = null;
        List<GameState> expandState = expandState(gameState, false);
        for(GameState s: expandState){
            GameSearchOutput curr = maxValue(s,alpha,beta,timeToCutoff-1);
            if (v > curr.getScore()){
                v = curr.getScore();
                minState = s;
            }
            if (v <= alpha){
                return new GameSearchOutput(minState, v);
            }
            beta = Math.min(beta,v);
        }
        return new GameSearchOutput(minState, v);
    }



}
