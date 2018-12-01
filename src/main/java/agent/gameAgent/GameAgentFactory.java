package agent.gameAgent;

import agent.Agent;
import simulator.SimulatorContext;

public class GameAgentFactory {

    private SimulatorContext context;

    public GameAgentFactory(SimulatorContext context){
        this.context = context;
    }
    public Agent getAgent(int agentType) {

        switch (agentType) {

            case 1:
                return new AdversrialAgent(context);

            default:
                return null;
        }
    }
}
