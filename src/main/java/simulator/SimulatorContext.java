package simulator;

import config.HurricaneGraph;
import entities.GameType;
import lombok.Getter;
import lombok.Setter;

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

    public SimulatorContext() {
    }

    public SimulatorContext(SimulatorContext cloneContext){
        this.graph = cloneContext.graph.clone();
        this.deadline = cloneContext.getDeadline();
        this.k = cloneContext.getK();
        this.time = cloneContext.getTime();
    }


}
