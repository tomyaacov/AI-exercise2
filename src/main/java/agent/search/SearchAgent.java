package agent.search;

import agent.Agent;
import agent.AgentAction;
import config.HurricaneGraph;
import config.HurricaneNode;
import states.State;
import lombok.Getter;
import lombok.Setter;
import org.graphstream.graph.Edge;
import simulator.SimulatorContext;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SearchAgent extends Agent{

    @Setter @Getter
    private int expandGlobalCounter;
    private final int maxExpand;
    private List<HurricaneNode> path;

    public SearchAgent(SimulatorContext context, int maxExpand) {
        super(context);
        this.expandGlobalCounter = 0;
        this.maxExpand = maxExpand;
        this.path = new LinkedList<>();
    }

    @Override
    public AgentAction doNextAction(double currTime){
        if(path.isEmpty()){
            List<State> stateList = search(new State(this.context, getCurrNode(), null, getPeople(), 0));
            path = stateList.stream()
                    .map(state -> state.getCurrNode())
                    .collect(Collectors.toList());
        }
        HurricaneNode nextNode = path.remove(0);
        HurricaneNode currNode = getCurrNode();
        if (isOutOfTime(nextNode, currNode)) {
            return null;
        }
        setCurrNode(nextNode);
        return new AgentAction(nextNode,
                calculateTraverseOperation(nextNode.getEdgeBetween(currNode)));
    }

    private boolean isOutOfTime(HurricaneNode nextNode, HurricaneNode currNode) {
        if(nextNode.equals(currNode)){
            return true;
        }
        return false;
    }


    public List<State> search(State currState){
        PriorityQueue<State> fringe = new PriorityQueue<State>();
        int expandCounter = 0;
        fringe.add(currState);
        while(! fringe.isEmpty()){
            State s = fringe.poll();
            if (s.isGoalState() || expandCounter == this.maxExpand){
                return getStatePath(s);
            }
            fringe.addAll(expand(s));
            expandCounter++;
            expandGlobalCounter++;
        }
        return null;
    }

    private List<State> expand(State s) {
        List<State> expandState = new LinkedList<>();
        Iterator<Edge> it = s.getCurrNode().getEdgeIterator();
        while (it.hasNext()) {
            Edge currentEdge = it.next();
            if (!HurricaneGraph.isEdgeBlock(currentEdge)) {
                HurricaneNode node = currentEdge.getOpposite(s.getCurrNode());
                expandState.add(constructNewState(s, currentEdge, node));
            }
        }
        return expandState;
    }

    private State constructNewState(State s, Edge currentEdge, HurricaneNode node) {
        if (s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()) > context.getDeadline()){
            return new State(s,
                    s.getCurrNode(),
                    s.getPeopleInNodes(),
                    s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()),
                    s.getPeople(),
                    s.getCostSoFar());
        }
        Map<String, Integer> peopleMap = new HashMap<>(s.getPeopleInNodes());
        if (node.isShelter()) {
            peopleMap.put(node.getId(), node.getPeople() + s.getPeople());
        } else {
            peopleMap.put(node.getId(), 0);
        }
        return new State(s,
                node,
                peopleMap,
                s.getTime() + calculateTraverseSearchOperation(currentEdge, s.getPeople()),
                node.isShelter() ? 0 : s.getPeople() + node.getPeople(),
                calculateNextStateCost(s, currentEdge));
    }

    protected double calculateNextStateCost(State s, Edge currentEdge) {
        return s.getCostSoFar() + calculateTraverseSearchOperation(currentEdge, s.getPeople());
    }

    private double calculateTraverseSearchOperation(Edge e, int peopleInVehicle){
        return e.getNumber("weight") * (1 + context.getK() * peopleInVehicle);

    }

    private Map<String, Integer> updatePeopleInNodesMap(State s, HurricaneNode node) {
        Map<String, Integer> peopleInNodes = new HashMap<>(s.getPeopleInNodes());
        peopleInNodes.put(node.getId(), 0);

        return peopleInNodes;
    }

    private List<State> getStatePath(State s) {
        LinkedList<State> statePath = new LinkedList<>();
        while (s.getPrev() != null){
            statePath.addFirst(s);
            s = s.getPrev();
        }
        return statePath;
    }

    public double calculatePerformanceMeasure(){
        int sumRescued = 0;
        Iterator<HurricaneNode> it = context.getGraph().getNodeIterator();
        while (it.hasNext()) {
            HurricaneNode currentNode = it.next();
            if (currentNode.isShelter()) {
                sumRescued += currentNode.getPeople();
            }
        }
        return context.getF()*sumRescued + expandGlobalCounter;
    }

}
