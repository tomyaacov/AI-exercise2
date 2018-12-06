package agent.gameAgent;

import agent.AgentAction;
import simulator.SimulatorContext;

public class FullCooperativeAgent extends GameAgent {

    public FullCooperativeAgent(SimulatorContext context){
        super(context);
    }

    @Override
    public AgentAction doNextAction(double currTime) {
        return null;
    }
}
