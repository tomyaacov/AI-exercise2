# Introduction to Artificial Intelligence - Assignment 2
##### Avi Rosen 303079511<br>
##### Tom Yaacov 305578239<br>
<br>
The assignment is written in java as a maven project.
<br>
<br>
We used GraphStream open source package for graph representaion.
<br><br>

##### <u>The heuristic functions we chose are:</u><br>

| Game type | Heuristic Evaluation Function |
| :---: | :---: |
| Adversarial (zero sum) | (SavedA + possibleSavedA/2) - (SavedB + possibleSavedB/2) |
| Semi-Cooperative | (SavedA + possibleSavedA/2) ; (SavedA + possibleSavedA/2) (Sorting is made by first, ties breaken by second)|
| Fully Cooperative | (SavedA + possibleSavedA/2) + (SavedB + possibleSavedB/2) |
<br>

##### <u>Example Scenario:</u>
The following example illustrates a scenario where the optimal behavior differs for the 3 kinds of games:<br>
<u>Input File:</u><br>
V 5    ; number of vertices n in config (from 1 to n)<br>
E 1 2 W4                 ; Edge from vertex 1 to vertex 2, weight 4<br>
E 2 4 W1                 ; Edge from vertex 2 to vertex 4, weight 1<br>
E 4 3 W1                 ; Edge from vertex 4 to vertex 3, weight 1<br>
E 1 3 W1                 ; Edge from vertex 1 to vertex 3, weight 1<br>
E 1 5 W1                 ; Edge from vertex 1 to vertex 5, weight 1<br>
<br>
V 4 P 3                  ; Vertex 4 initially contains 3 person to be rescued<br>
V 1 S                    ; Vertex 1 contains a hurricane shelter<br>
V 3 P 2                  ; Vertex 3 initially contains 2 persons to be rescued<br>
V 5 P 1                  ; Vertex 5 initially contains 1 persons to be rescued<br>
<br>
D 5                     ; Deadline is at time 5<br>

<u>Additional Parameters:</u><br>
K constant is 0<br>
Cutoff value is 10<br>
Agent 1 initial position is node 2<br>
Agent 2 initial position is node 1<br>
 
##### <u>Adversarial (zero sum) output:</u>
Agent 1 initial position is node 2<br>
Agent 2 initial position is node 1<br>
<br>
agent 1is in node 1; time is: 4.0; deadline: 5; agent have 0 people in vehicle<br>
Operation failed - agent 2is in node 1; time is: 8.0; deadline: 5; agent have 0 people in vehicle<br>
Simulation over. press to see agents scores<br>
<br>
Agent 1 score is 0<br>
Agent 2 score is 0<br>
 
##### <u>Semi-Cooperative output:</u>
Agent 1 initial position is node 2<br>
Agent 2 initial position is node 1<br>
<br>
agent 1is in node 4; time is: 1.0; deadline: 5; agent have 3 people in vehicle<br>
agent 2is in node 3; time is: 2.0; deadline: 5; agent have 2 people in vehicle<br>
agent 1is in node 3; time is: 3.0; deadline: 5; agent have 3 people in vehicle<br>
agent 2is in node 1; time is: 4.0; deadline: 5; agent have 0 people in vehicle<br>
agent 1is in node 1; time is: 5.0; deadline: 5; agent have 0 people in vehicle<br>
Simulation over. press to see agents scores<br>
<br>
Agent 1 score is 3<br>
Agent 2 score is 2<br>
##### <u>Fully Cooperative output:</u>
Agent 1 initial position is node 2<br>
Agent 2 initial position is node 1<br>
