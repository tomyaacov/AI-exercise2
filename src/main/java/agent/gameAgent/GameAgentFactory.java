package agent.gameAgent;

import agent.Agent;
import agent.HumanAgent;
import simulator.SimulatorContext;

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
                return new AdversrialAgent(context);
            default:
                return null;
        }
    }
}
