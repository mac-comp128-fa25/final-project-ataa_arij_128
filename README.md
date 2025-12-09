# Gesture Rush

## Description
Gesture Rush is a single player game where gestures continually fall from the sky. The user's task is to trace the gestures and erase as many points from each gesture as possible before the gesture reaches the bottom. The more points the user can erase, the higher score they get. The user has 90 seconds to erase as many points from the gestures as they can. If the user misses 200 points, or 90 second timer ends before that happens, that game/round is over and the user has the option to play again and beat their top score.

## Data Structures
The program uses Lists, Queues, and Arrays to score things like points, line segments, scores, medals, and gesture templates. Each data structure was carefully chosen in order to get the most efficient worst case time complexity for the removals, adds, and lookups made in the program.

## Implementation
**1. Gestures:** Each gesture has a method in the primary game app which creates it and saves it as a template. These templates are used in a falling gesture class which allows gestures to fall down a canvas and be erased.

**2. Eraser:** A single class is used for the eraser which erases points and updates the number of points it erased as an instance variable.

**3. Score:** A score class is created to handle all the scores. In this class, the user’s current and top score are stored as instance variables and a queue stores the history of the user's scores. A method in Score gives the user their GameResult (another wrapper class containing an integer score and a medal (class for ranking system essentially)) which figures out their current score, what medal they got as a result, and updates their top score if necessary. The Score class also contains a method which takes the number of points erased and recalculates it so it is based out of 5.

**4. Menus:** A menu, end game, and app class used to set up the different menus for the game

**5. Gesture Rush Game Class:** The GestureRushGame class puts everything together. The canvas and text for the game is set up. The class stores a list of template gestures and a list of removed points. A random gesture from the list of template gestures is chosen as the current FallingGesture which falls and the player can erase. The update method controls the speed of the falling gestures, the time remaining, and updates the score as the player removes points from gestures (helper methods are used to update the labels). Once the game is completed, the Score type instance variable stores the player’s score, updating the Score queue and other instance variables as need be, and sends the player to the endgame menu.
