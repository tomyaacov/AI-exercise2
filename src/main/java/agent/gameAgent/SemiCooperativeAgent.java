package agent.gameAgent;

import agent.AgentAction;
import simulator.SimulatorContext;

public class SemiCooperativeAgent extends GameAgent{

    public SemiCooperativeAgent(SimulatorContext context) {
        super(context);
    }

    @Override
    public AgentAction doNextAction(double currTime) {
        return null;
    }
}
