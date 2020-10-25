# Breaktru
 ISG Breaktru AI

## How to run the application?
    Double click the jar file

    An output stream window should open and your internet browser should open
    If the internet windows has opend, to start the game, you press initialize/restart button the intialize the board.

    You need to press one of the following 3 buttons to really start playing:
        - Play as Gold (Gold Human, Silver AI)
        - Play as Silver (Gold AI, Silver Human)
        - Both Sides AI  (For this function, the remaining time funcitonality is  not working correctly)

    Then you can start playing. Everytime the AI has to make a move, press the AI move calc button.

    If it is the human's turn, you need to press the piece you want to move, and the position you want to move your piece to. After this, you press the move button.

    The undo button's functionality is not tested fully, and might break the game!

## File organization
    There are 3 main folders:
        src\main\kotlin\com\breaktru\app
            this is the spring boot server, here I specified the possible API calls
        src\main\kotlin\com\breaktru\code
            Here all the functionality for the alpha beta search is stored
            -searchAlgorithmFunctions.kt is the file with the AB-search algorithm.
            -EvalFunction.kt is the file with all the evaluation functions
            -MoveGeneration.kt is the file with the moveGenerator
            -TranspositionTable.kt is the file regarding the transposition table, zobrist key generation and storing/receiving values
        src\main\kotlin\resources
            Here all the files for the frontend are stored

    For all the files hold, if the file name has OLD in the name, this is old code and the code has not been cleaned. In the file OldSearchFunctions.kt, you can see all the alpha beta search functions I considered.

## Start from intellij
    If you want to start the application from intellij, please run the following command in the main folder: gradlew.bat bootrun
    If you want to build a new jar, please run the follwoing command in the main folder: gradlew.bat bootjar