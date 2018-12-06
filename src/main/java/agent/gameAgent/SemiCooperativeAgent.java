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

public class SemiCooperativeAgent extends GameAgent{

    public SemiCooperativeAgent(SimulatorContext context) {
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
        AgentAction action = getDecision(gameState);
        setCurrNode(action.getCurrNode());

        return action;
    }

    private AgentAction getDecision(GameState gameState){

        GameSearchOutputSemi optimal = maxAValue(gameState, getContext().getCutoff());
        context.getAgentStates().get(getId()).setPeopleInCar(optimal.getGameState().getPeople());
        context.getAgentStates().get(getId()).setPosition(optimal.getGameState().getCurrNode());
        context.getAgentStates().get(getId()).setSavedPeople(optimal.getGameState().getMineSavedPeople());


        return new AgentAction(optimal.getGameState().getCurrNode(), optimal.getGameState().getTime()- optimal.getGameState().getPrev().getTime());
    }

    private GameSearchOutputSemi maxAValue(GameState gameState, int timeToCutoff){
        if (gameState.isGoalState()){
            return new GameSearchOutputSemi(gameState, gameState.getMineSavedPeople(), gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return Algorithm.semiHeuristicStaticEvaluation(gameState, context);
        }
        double v = -Double.MAX_VALUE;
        double vb = -Double.MAX_VALUE;
        GameState maxState = null;
        List<GameState> expandState = expandState(gameState, true);
        for(GameState s: expandState){
            GameSearchOutputSemi curr = maxBValue(s,timeToCutoff-1);
            if((v < curr.getAScore()) || (v == curr.getAScore() && vb < curr.getBScore())){
                v = curr.getAScore();
                vb = curr.getBScore();
                maxState = s;
            }
        }
        return new GameSearchOutputSemi(maxState, v, vb);
    }

    private GameSearchOutputSemi maxBValue(GameState gameState, int timeToCutoff){
        if (gameState.isGoalState()){
            return new GameSearchOutputSemi(gameState, gameState.getMineSavedPeople(), gameState.getOtherSavedPeople());
        }
        if (timeToCutoff == 0){
            return Algorithm.semiHeuristicStaticEvaluation(gameState, context);
        }
        double v = -Double.MAX_VALUE;
        double vb = -Double.MAX_VALUE;
        GameState maxState = null;
        List<GameState> expandState = expandState(gameState, true);
        for(GameState s: expandState){
            GameSearchOutputSemi curr = maxAValue(s, timeToCutoff-1);
            if((vb < curr.getBScore()) || (vb == curr.getBScore() && v < curr.getAScore())){
                v = curr.getAScore();
                vb = curr.getBScore();
                maxState = s;
            }
        }
        return new GameSearchOutputSemi(maxState, v, vb);
    }
}
