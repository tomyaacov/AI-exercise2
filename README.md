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
The following example illustrates a scenario where the optimal behavior differs for the 3 kinds of games:
 # Example description
 
##### <u>Adversarial (zero sum) output:</u>
# Output...
 
##### <u>Semi-Cooperative output:</u>
# Output...

##### <u>Fully Cooperative output:</u>
# Output...
