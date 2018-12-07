package algorithms;

import agent.gameAgent.GameSearchOutputSemi;
import config.HurricaneGraph;
import config.HurricaneNode;
import states.GameState;
import states.State;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import org.graphstream.ui.view.Viewer;
import parser.Parser;
import simulator.Simulator;
import simulator.SimulatorContext;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {

    public static DijkstraOutput dijkstra(SimulatorContext simulatorContext, HurricaneNode source, int sourceCarrying){
        Map<String, Integer> peopleInNodes = initializePeopleInside(simulatorContext);
        return dijkstra(simulatorContext,peopleInNodes, source, sourceCarrying);
    }


        public static DijkstraOutput dijkstra(SimulatorContext simulatorContext, Map<String, Integer> peopleInNodes, HurricaneNode source, int sourceCarrying){

        HurricaneNode u, v;
        Double alt;
        Edge currentEdge;
        List<HurricaneNode> Q = new ArrayList<HurricaneNode>(simulatorContext.getGraph().getNodeCount()+1);
        List<Double> dist = new ArrayList<Double>(simulatorContext.getGraph().getNodeCount()+1);
        List<HurricaneNode> prev = new ArrayList<HurricaneNode>(simulatorContext.getGraph().getNodeCount()+1);
        List<Integer> nodeCarrying = new ArrayList<Integer>(simulatorContext.getGraph().getNodeCount()+1);


        Q.add(0, null);
        dist.add(0, Double.MAX_VALUE);
        prev.add(0, null);
        nodeCarrying.add(0,null);


        for(Node n:simulatorContext.getGraph()) {
            Q.add(Integer.parseInt(n.getId()), (HurricaneNode)n);
            dist.add(Integer.parseInt(n.getId()), Double.MAX_VALUE);
            prev.add(Integer.parseInt(n.getId()), null);
            nodeCarrying.add(Integer.parseInt(n.getId()), 0);

        }

        dist.set(Integer.parseInt(source.getId()), 0.0);
        nodeCarrying.set(Integer.parseInt(source.getId()), sourceCarrying);

        Integer minDist;

        while (true){
            minDist = getMinDist(dist, Q);
            if (minDist == null){break;}
            u = Q.set(minDist, null);


            Iterator<Edge> it = u.getEdgeIterator();
            while (it.hasNext()) {
                currentEdge = it.next();
                if (!HurricaneGraph.isEdgeBlock(currentEdge)) {
                    v = currentEdge.getOpposite(u);
                    alt = dist.get(Integer.parseInt(u.getId())) + currentEdge.getNumber("weight")
                            * (1 + simulatorContext.getK() * nodeCarrying.get(Integer.parseInt(u.getId())));
                    if (alt < dist.get(Integer.parseInt(v.getId()))) {
                        dist.set(Integer.parseInt(v.getId()), alt);
                        prev.set(Integer.parseInt(v.getId()), u);
                        if (!v.isShelter()) {
                            nodeCarrying.set(Integer.parseInt(v.getId()), nodeCarrying.get(Integer.parseInt(u.getId())) + peopleInNodes.get(v.getId()));
                        }
                    }
                }
            }
        }

        return new DijkstraOutput(dist, prev, nodeCarrying);
    }

    public static Integer getMinDist(List<Double> dist, List<HurricaneNode> Q){
        Double currMinDist = Double.MAX_VALUE;
        Integer result = null;
        for (int i = 1; i < dist.size(); i++){
            if(currMinDist > dist.get(i) && Q.get(i) != null){
                currMinDist = dist.get(i);
                result = i;
            }
        }
        return result;
    }

    public static double heuristicFunction(State state){
        int dead = 0;
        SimulatorContext simulatorContext = Simulator.getContext();
        DijkstraOutput wayForNodeWithPeople = dijkstra(simulatorContext,
                                                        state.getPeopleInNodes(),
                                                        state.getCurrNode(),
                                                        state.getPeople());
        for(int i = 1; i<=simulatorContext.getGraph().getNodeCount(); i++){
            HurricaneNode currNode = simulatorContext.getGraph().getNode(String.valueOf(i));
            if(!currNode.isShelter() && state.getPeopleInNodes().get(String.valueOf(i)) > 0){
                Map<String, Integer> wayForShelterPeopleInNode = getUpdatedPeopleInNodesMap(state, wayForNodeWithPeople, currNode);

                DijkstraOutput wayForShelter = dijkstra(simulatorContext, wayForShelterPeopleInNode, currNode, wayForShelterPeopleInNode.get(currNode.getId()));

                double minDistToShelter = getDistToClosestShelter(simulatorContext, wayForShelter);

                if(wayForNodeWithPeople.getDist().get(i) + minDistToShelter > simulatorContext.getDeadline()-state.getTime()){
                    dead += state.getPeopleInNodes().get(currNode.getId());
                }
            }
        }
        double minDistToShelter = getDistToClosestShelter(simulatorContext, wayForNodeWithPeople);
        if(minDistToShelter > simulatorContext.getDeadline()-state.getTime()){
            dead += state.getPeople();
        }
        return dead;
    }

    private static Map<String, Integer> getUpdatedPeopleInNodesMap(State state, DijkstraOutput wayForNodeWithPeople, HurricaneNode currNode) {
        Map<String, Integer> wayForShelterPeopleInNode = new HashMap<>(state.getPeopleInNodes());
        ArrayList<HurricaneNode> wayToShelterStack = new ArrayList<>();
        HurricaneNode traverse = currNode;
        while (wayForNodeWithPeople.getPrev().get(Integer.valueOf(traverse.getId())) != null){
            wayToShelterStack.add(0, traverse);
            traverse = wayForNodeWithPeople.getPrev().get(Integer.valueOf(traverse.getId()));
        }
        wayToShelterStack.add(0, traverse);

        int sumPeople = 0;
        for(int j = 0; j < wayToShelterStack.size()-1; j++){
            if(wayToShelterStack.get(j).isShelter()){
                wayForShelterPeopleInNode.put(wayToShelterStack.get(j).getId(), wayForShelterPeopleInNode.get(wayToShelterStack.get(j).getId()) + sumPeople);
            } else {
                sumPeople += wayForShelterPeopleInNode.get(wayToShelterStack.get(j).getId());
                wayForShelterPeopleInNode.put(wayToShelterStack.get(j).getId(), 0);
            }
        }
        wayForShelterPeopleInNode.put(currNode.getId(), wayForShelterPeopleInNode.get(currNode.getId()) + sumPeople);
        return wayForShelterPeopleInNode;
    }

    private static double getDistToClosestShelter(SimulatorContext simulatorContext, DijkstraOutput dijkstraOutput) {
        double minDistToShelter = Double.MAX_VALUE;
        for (int j = 1; j < dijkstraOutput.getDist().size(); j++) {
            HurricaneNode shelterCandidate = simulatorContext.getGraph().getNode(String.valueOf(j));
            if (shelterCandidate.isShelter() && dijkstraOutput.getDist().get(j) < minDistToShelter) {
                minDistToShelter = dijkstraOutput.getDist().get(j);
            }
        }
        return minDistToShelter;
    }

    private static Map<String, Integer> initializePeopleInside(SimulatorContext simulatorContext){
        Map<String, Integer> peopleInNodes= new HashMap<>();
        HurricaneNode checkedNode;
        for(int i = 1; i <= simulatorContext.getGraph().getNodeCount(); i++){
            checkedNode = simulatorContext.getGraph().getNode(Integer.toString(i));
            peopleInNodes.put(Integer.toString(i), checkedNode.getPeople());
        }
        return peopleInNodes;
    }

    public static double heuristicStaticEvaluation(GameState gameState, SimulatorContext context){
        State stateA = getStateA(gameState);
        State stateB = getStateB(gameState);

        double remainPeopleToSave = getRemainedPeopleToSave(gameState, context);
        double possibleSavedPeopleA = remainPeopleToSave + stateA.getPeople() - heuristicFunction(stateA);
        double possibleSavedPeopleB = remainPeopleToSave + stateB.getPeople() - heuristicFunction(stateB);
        return (gameState.getMineSavedPeople() + possibleSavedPeopleA/2) - (gameState.getOtherSavedPeople() + (possibleSavedPeopleB)/2);
    }

    public static double coopertativeHeuristicStaticEvaluation(GameState gameState, SimulatorContext context){
        State stateA = getStateA(gameState);
        State stateB = getStateB(gameState);

        double remainPeopleToSave = getRemainedPeopleToSave(gameState, context);
        double possibleSavedPeopleA = remainPeopleToSave + stateA.getPeople() - heuristicFunction(stateA);
        double possibleSavedPeopleB = remainPeopleToSave + stateB.getPeople() - heuristicFunction(stateB);
        return (gameState.getMineSavedPeople() + possibleSavedPeopleA/2) + (gameState.getOtherSavedPeople() + (possibleSavedPeopleB)/2);
    }

    public static GameSearchOutputSemi semiHeuristicStaticEvaluation(GameState gameState, SimulatorContext context){
        State stateA = getStateA(gameState);
        State stateB = getStateB(gameState);

        double remainPeopleToSave = getRemainedPeopleToSave(gameState, context);
        double possibleSavedPeopleA = remainPeopleToSave + stateA.getPeople() - heuristicFunction(stateA);
        double possibleSavedPeopleB = remainPeopleToSave + stateB.getPeople() - heuristicFunction(stateB);
        return new GameSearchOutputSemi(gameState, gameState.getMineSavedPeople() + possibleSavedPeopleA/2,
                gameState.getOtherSavedPeople() + possibleSavedPeopleB/2);
    }

    private static State getStateB(GameState gameState) {
        return new State(gameState.getPrev(),
                                        gameState.getOtherCurrNode(),
                                        new HashMap<>(gameState.getPeopleInNodes()),
                                        gameState.getTime(),
                                        gameState.getOtherPeople(),
                                        gameState.getOtherSavedPeople());
    }

    private static State getStateA(GameState gameState) {
        return new State(gameState.getPrev(),
                                        gameState.getCurrNode(),
                                        new HashMap<>(gameState.getPeopleInNodes()),
                                        gameState.getTime(),
                                        gameState.getPeople(),
                                        gameState.getMineSavedPeople());
    }

//    private static double getRemainPeopleToSave(GameState gameState, SimulatorContext context) {
//        return (double) gameState.getPeopleInNodes().entrySet().stream()
//                    .filter(node->  {
//                        HurricaneNode curr =context.getGraph().getNode(node.getKey());
//                        return ! curr.isShelter();
//                    })
//                    .mapToInt(node->  {
//                        HurricaneNode curr =context.getGraph().getNode(node.getKey());
//                        return curr.getPeople();
//                    })
//                    .sum();
//    }

    private static double getRemainedPeopleToSave(GameState gameState, SimulatorContext context) {
        int sum = 0;
        for (Map.Entry<String, Integer> entry : gameState.getPeopleInNodes().entrySet()) {
            HurricaneNode curr =context.getGraph().getNode(entry.getKey());
            if (!curr.isShelter()){
                sum += gameState.getPeopleInNodes().get(entry.getKey());
            }
        }
        return sum;
    }


//    public static double gameHeuristicFunction(State myState, State otherState){
//        int dead = 0;
//        SimulatorContext simulatorContext = Simulator.getContext();
//        DijkstraOutput wayForNodeWithPeople = dijkstra(simulatorContext,
//                myState.getPeopleInNodes(),
//                myState.getCurrNode(),
//                myState.getPeople());
//        for(int i = 1; i<=simulatorContext.getGraph().getNodeCount(); i++){
//            HurricaneNode currNode = simulatorContext.getGraph().getNode(String.valueOf(i));
//            if(!currNode.isShelter() && myState.getPeopleInNodes().get(String.valueOf(i)) > 0){
//                Map<String, Integer> wayForShelterPeopleInNode = getUpdatedPeopleInNodesMap(myState, wayForNodeWithPeople, currNode);
//
//                DijkstraOutput wayForShelter = dijkstra(simulatorContext, wayForShelterPeopleInNode, currNode, wayForShelterPeopleInNode.get(currNode.getId()));
//
//                double minDistToShelter = getDistToClosestShelter(simulatorContext, wayForShelter);
//
//                if(wayForNodeWithPeople.getDist().get(i) + minDistToShelter > simulatorContext.getDeadline()-myState.getTime()){
//                    dead += myState.getPeopleInNodes().get(currNode.getId());
//                }
//            }
//        }
//        double minDistToShelter = getDistToClosestShelter(simulatorContext, wayForNodeWithPeople);
//        if(minDistToShelter > simulatorContext.getDeadline()-myState.getTime()){
//            dead += myState.getPeople();
//        }
//
//        DijkstraOutput otherWayForNodeWithPeople = dijkstra(simulatorContext,
//                otherState.getPeopleInNodes(),
//                otherState.getCurrNode(),
//                otherState.getPeople());
//        double otherMinDistToShelter = getDistToClosestShelter(simulatorContext, otherWayForNodeWithPeople);
//        if(otherMinDistToShelter <= simulatorContext.getDeadline()-otherState.getTime()){
//            dead += otherState.getPeople();
//        }
//        return dead;
//    }

    public static void main(String[] args) throws Exception{
        Parser parser = new Parser();
        SimulatorContext context = parser.parseFile("src.main.resources.graph_dijkstra_test".replace(".", File.separator));
        context.setK(1);
        Viewer view = context.getGraph().display();
        HurricaneNode source = context.getGraph().getNode("2");
        DijkstraOutput output = Algorithm.dijkstra(context, source, 1);
        System.out.println(output);

    }


}
