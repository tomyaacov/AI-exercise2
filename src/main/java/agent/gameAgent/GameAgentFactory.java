package agent.gameAgent;

import agent.Agent;
import agent.HumanAgent;
import simulator.SimulatorContext;
import states.GameType;

public class GameAgentFactory {

    private SimulatorContext context;

    public GameAgentFactory(SimulatorContext context){
        this.context = context;
    }
    public Agent getAgent(int agentType, int id) {

        switch (agentType) {

            case 1:
                return new GameHumanAgent(context);
            case 2:
                switch (context.getGameType()){
                    case ADVERSARIAL:
                        return new AdversrialAgent(context);
                    case SEMI_COOPERATIVE:
                        return new SemiCooperativeAgent(context);
                    case FULLY_COOPERATIVE:
                        return new FullCooperativeAgent(context);
                }
            default:
                return null;
        }
    }
}
