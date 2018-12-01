package simulator;

import agent.Agent;
import agent.AgentAction;
import agent.AgentFactory;
import agent.gameAgent.GameAgentFactory;
import agent.search.SearchAgent;
import config.HurricaneNode;
import entities.GameType;
import lombok.Getter;
import lombok.Setter;
import org.graphstream.ui.view.Viewer;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Simulator {

    @Getter @Setter
    static SimulatorContext context;

    @Getter @Setter
    private List<Agent> agents;

    @Getter @Setter
    private GameAgentFactory agentFactory;

    @Getter @Setter
    private Parser parser;


    public void run(){
        initialize();
        Viewer view = context.getGraph().display();
        pickUpPeopleInInitPosition();
        play();
        wrapUp();

    }

    private void wrapUp() {
        PressToSeeNextMove("Simulation over. press to see agents performance measure");
        printPerformanceMeasure();
    }

    private void play() {
        int i = 0;
        while(isGameOn(context.getTime())){
            Agent agent = agents.get(i);
            AgentAction action = agent.doNextAction(context.getTime());
            if(action == null){
                context.getGraph().setAttribute("ui.title",  "time is UP. Agent " + (i+1) + " last opertaion failed");
                context.getGraph().display();
                return;
            }
            context.setTime(context.getTime() + action.getTime());
            agent.doActionInNode();
            setAgentState(i, agent, context.getTime());
            PressToSeeNextMove("press to continue");
            i = updateI(i);
        }
        if (isGoalAchieved()){
            context.getGraph().setAttribute("ui.title",  "goal achieved!");
            context.getGraph().display();
        } else {
            context.getGraph().setAttribute("ui.title",  "time is UP.");
            context.getGraph().display();
        }
        

    }

    private void PressToSeeNextMove(String s) {
        System.out.println(s);
        Scanner input = new Scanner(System.in);
        input.next();
    }

    private int updateI(int i) {
        i++;
        if(i == agents.size()){
            i = 0;
        }
        return i;
    }

    private void printPerformanceMeasure() {
        for (int i = 0; i < agents.size(); i++ ){
            Agent agent = agents.get(i);
            if(agent instanceof SearchAgent){
                System.out.println("Agent " + (i+1) + " performance measure is " + ((SearchAgent) agent).calculatePerformanceMeasure());
            }
        }
    }

    private boolean isGameOn(double time) {
        return isLeftTime(time) && !isGoalAchieved();
    }

    private boolean isLeftTime(double time) {
        return time < context.getDeadline();
    }

    private boolean isGoalAchieved() {
        for (int i = 1; i <= context.getGraph().getNodeCount(); i++){
            HurricaneNode node = context.getGraph().getNode(String.valueOf(i));
            if (!node.isShelter() && node.getPeople() > 0){
                return false;
            }
        }
        return ! agents.stream().anyMatch(agent -> agent.getPeople()>0);
    }

    private void pickUpPeopleInInitPosition() {
        for(Agent agent : agents){
            agent.doActionInNode();
            HurricaneNode currNode = agent.getCurrNode();
            currNode.setAttribute("ui.label", currNode);
        }
        setInitStateInViewerTitle();
    }

    private void setInitStateInViewerTitle() {
        for (int i=0; i<agents.size(); i++){
            setAgentState(i, agents.get(i), 0);
        }
    }

    private void setAgentState(int i, Agent agent, double time) {
        String agentState = "agent " + (i+1) + "is in node " + agent.getCurrNode().getId() + "; time is: "
                +time + "; deadline: " + context.getDeadline() + "; agent have " +
                agent.getPeople() + " people in vehicle";

        agent.getCurrNode().setAttribute("ui.label", agent.getCurrNode());
        context.getGraph().setAttribute("ui.title",  agentState);
    }


    private void initialize(){

        Scanner input = new Scanner(System.in);
        agents = new LinkedList<>();
        parser = new Parser();
        context = initializeGraph();
        context.setTime(0);
        agentFactory = new GameAgentFactory(context);

        System.out.println("Welcome to Agent simulator");
        System.out.println("Please enter a value for K constant:");
        context.setK(input.nextDouble());
//        System.out.println("Please enter a value for f:"); //TODO remove?
//        context.setF(input.nextDouble());
        System.out.print("Please specify the game type:  1) Adversarial   2)  semi-cooperative  3) fully cooperative  ");
        setGameType(input);
        System.out.println("Please enter a value for cutoff:");
        context.setCutoff(input.nextInt());
        initializeAgent(input, 0);
        initializeAgent(input, 1);

    }

    private void setGameType(Scanner input) {
        switch (input.nextInt()){
            case 1:
                context.setGameType(GameType.ADVERSARIAL);
                break;
            case 2:
                context.setGameType(GameType.SEMI_COOPERATIVE);
                break;
            case 3:
                context.setGameType(GameType.FULLY_COOPERATIVE);
                break;
            default:
                System.out.print("error");
        }
    }

    private void initializeAgent(Scanner input, int i) {
        System.out.println("Agent no. " + (i+1) + ":");
        System.out.println("Please specify agent Type: 1) Humam  2) game tree search agent");
        int type = input.nextInt();
        System.out.println("Please specify Agent " + (i+1) +
                " initial position (number in between 1 to " + context.getGraph().getNodeCount() +"):");
        int position = input.nextInt();
        Agent agent = agentFactory.getAgent(type);
        agent.setCurrNode(context.getGraph().getNode(String.valueOf(position)));
        agents.add(agent);
    }

    private SimulatorContext initializeGraph() {
        try {
            return parser.parseFile("src.main.resources.graph".replace(".", File.separator));
        } catch (IOException e) {
            System.err.println("parsing 'graph.txt' file encountered a problem. Check for valid input");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        Simulator s= new Simulator();
        s.run();
    }
}
