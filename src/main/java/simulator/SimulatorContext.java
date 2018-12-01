package simulator;

import config.HurricaneGraph;
import entities.GameType;
import lombok.Getter;
import lombok.Setter;
import states.AgentState;

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
    }

    public SimulatorContext(SimulatorContext cloneContext){
        this.graph = cloneContext.graph.clone();
        this.deadline = cloneContext.getDeadline();
        this.k = cloneContext.getK();
        this.time = cloneContext.getTime();
        this.agentStates = new ArrayList<>(2);
    }


}
