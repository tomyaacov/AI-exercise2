package agent.gameAgent;

import agent.HumanAgent;
import simulator.SimulatorContext;
import states.AgentState;

public class GameHumanAgent extends HumanAgent {

    public GameHumanAgent(SimulatorContext context) {
        super(context);

    }

    @Override
    public void doActionInNode() {
        AgentState state = context.getAgentStates().get(this.getId());
        if (getCurrNode().isShelter()) {
            getCurrNode().setPeople(getPeople() + getCurrNode().getPeople());
            setPeople(0);
            state.setPeopleInCar(0);
        } else {
            setPeople(getPeople() + getCurrNode().getPeople());
            getCurrNode().setPeople(0);
            state.setPeopleInCar(getPeople());
        }

    }

}
