package states;

import algorithms.Algorithm;
import config.HurricaneNode;
import lombok.Getter;
import lombok.Setter;
import simulator.Simulator;
import simulator.SimulatorContext;

import java.util.HashMap;
import java.util.Map;

public class State implements Comparable{

    @Getter @Setter
    protected State prev;

    @Getter @Setter
    protected HurricaneNode currNode;

    @Getter @Setter
    protected Map<String, Integer> peopleInNodes;

    @Getter @Setter
    protected double time;

    @Getter @Setter
    protected int people;

    @Getter @Setter
    protected double costSoFar;

    public State() {
    }

    public State(State prev, HurricaneNode currNode, Map<String, Integer> peopleInside, double time, int people, double costSoFar) {
        this.prev = prev;
        this.currNode = currNode;
        this.peopleInNodes = peopleInside;
        this.time = time;
        this.people = people;
        this.costSoFar = costSoFar;
    }

    public State(SimulatorContext simulatorContext, HurricaneNode currNode, State prev, int people, double costSoFar) {
        this.prev = prev;
        this.currNode = currNode;
        this.time = simulatorContext.getTime();
        this.people = people;
        this.costSoFar = costSoFar;
        this.peopleInNodes = initializePeopleInside(simulatorContext);
    }

    public Boolean isGoalState(){
        SimulatorContext simulatorContext = Simulator.getContext();
        if(simulatorContext.getDeadline() <= time){return true;}
        for (int i=1; i <= simulatorContext.getGraph().getNodeCount(); i++){
            HurricaneNode currNode = simulatorContext.getGraph().getNode(String.valueOf(i));
            if(peopleInNodes.get(String.valueOf(i)) >= 0 && !currNode.isShelter()){
                return false;
            }
        }
        return people==0;
    }

    private Map<String, Integer> initializePeopleInside(SimulatorContext simulatorContext){
        Map<String, Integer> peopleInNodes= new HashMap<>();
        HurricaneNode checkedNode;
        for(int i = 1; i <= simulatorContext.getGraph().getNodeCount(); i++){
            checkedNode = simulatorContext.getGraph().getNode(Integer.toString(i));
            peopleInNodes.put(Integer.toString(i), checkedNode.getPeople());
        }
        return peopleInNodes;
    }


    @Override
    public int compareTo(Object o) {
        State s =(State)o;
        double diff = (getCostSoFar() + Algorithm.heuristicFunction(this)
                - (s.getCostSoFar() + Algorithm.heuristicFunction(s)));
        if(diff <0){
            return -1;
        } else if(diff > 0 ){
            return 1;
        }
        return 0;
    }


    public static void main(String[] args) {

    }
}
