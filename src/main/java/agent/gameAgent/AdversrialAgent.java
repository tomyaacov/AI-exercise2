package agent.gameAgent;

import agent.AgentAction;
import simulator.SimulatorContext;

public class AdversrialAgent extends GameAgent {

    public AdversrialAgent(SimulatorContext context){
        super(context);
    }

    @Override
    public AgentAction doNextAction(double currTime) {
        return null;
    }
}
