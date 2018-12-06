package simulator;

import config.HurricaneGraph;
import lombok.Getter;
import lombok.Setter;
import states.AgentState;
import states.GameType;

import java.util.ArrayList;
import java.util.List;

public class SimulatorContext {

    @Getter @Setter
    private HurricaneGraph graph;

    @Getter @Setter
    private int deadline;

    @Getter @Setter
    private double k;

    @Getter @Setter
    private double time;

    @Getter @Setter
    private double f;

    @Getter @Setter
    private GameType gameType;

    @Getter @Setter
    private int cutoff;

    @Getter @Setter
    private List<AgentState> agentStates;

    public SimulatorContext() {
        this.agentStates = new ArrayList<>(2);
    }

    public SimulatorContext(SimulatorContext cloneContext){
        this.graph = cloneContext.graph.clone();
        this.deadline = cloneContext.getDeadline();
        this.k = cloneContext.getK();
        this.time = cloneContext.getTime();
        this.agentStates = new ArrayList<>(2);
    }


}
