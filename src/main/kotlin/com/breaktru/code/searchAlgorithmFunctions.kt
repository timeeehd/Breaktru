package com.breaktru.code

//Alpha Beta search algorithm with the following enhancements: Move Ordering, Detailed evaluation function, Null Move and Transposition Table

fun alphaBetaEval3MoveOrdering3TTNullID(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean, startTime: Long): MutableMap<String, Any> {

    var finished = true
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)

    //For the iterative deepening, if the time spent is more than 10 seconds, I quit the search and say that the search was not finished
    if (System.currentTimeMillis() - startTime > 10000) {
        return mutableMapOf("score" to mutableListOf(-100000), "board" to boardInput, "tt" to ttTable, "finished" to false)
    }

    //Check for terminal node or if depth is 0, and return the value
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(finalEvaluation(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable, "finished" to true)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)
    var flag = ""
    var returnedBoard = Board()
    var result = Int.MIN_VALUE
    var moveType = ""

    var retrievedFrom: MutableList<Int> = mutableListOf()
    var retrievedTo: MutableList<Int> = mutableListOf()
    var retrievedScore: Int
    var calcScore = -10000
    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)

    //Check if the player already moved an escort ship and checks whether the transposition table move it suggested is a
    //capture move or a flagship move
    if (ttRetrieval["depth"] as Int >= 0) {
        retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        retrievedTo = ttRetrieval["to"] as MutableList<Int>
        retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val retrievedMoveType = ttRetrieval["movetype"] as String
        calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (remainingMoves == 1 && (retrievedMoveType == "capture" || retrievedMoveType == "flagship")) {
            retrievedFrom = mutableListOf()
            retrievedTo = mutableListOf()
        }
    }
    //regular Transposition table code
    if (ttRetrieval["depth"] as Int >= depth) {
        if (ttRetrieval["flag"] as String == "Exact") {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable, "finished" to true)
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable, "finished" to true)
        }
    }

    //null move, R = 1, and I don't to a null move at the root node or at depth = 1
    if (depth >= 2 && !rootNode) {
        if (playersTurn == "G") {
            var abResult = alphaBetaEval3MoveOrdering3TTNullID(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false, startTime)
            var bestMoveScore = abResult["score"] as MutableList<Int>
            returnedBoard = abResult["board"] as Board
            result = -bestMoveScore[0]
            finished = abResult["finished"] as Boolean
            if (!finished) result = -10000
        } else if (playersTurn == "S") {
            var abResult = alphaBetaEval3MoveOrdering3TTNullID(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false, startTime)
            var bestMoveScore = abResult["score"] as MutableList<Int>
            returnedBoard = abResult["board"] as Board
            result = -bestMoveScore[0]
            finished = abResult["finished"] as Boolean
            if (!finished) result = -10000
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable, "finished" to finished)
        }
    }
    score = Int.MIN_VALUE
    //get the possible moves
    val possibleMoves = finalMoveGeneration(boardCopy, playersTurn, remainingMoves, retrievedFrom, retrievedTo)

    // When checking all the moves, my possibleMoves variable is a map with 4 different key values:
    // for transposition table move, capture move, flagship move and an escort move
    // so I first loop over all the keys.
    // Then for all moves, I have the board position I consider as a key and then have a list of all board positions it can move to
    // so that's why I've still 2 more loops and I end up with 3 loops.
    outerloop@ for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                returnedBoard = Board()
                //calcRemainingMoves will check if it was a capture move, escort move or if the flagship was moved
                // and will say if the player still have a possibility to move another ship
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                // check if you moved a escort, so you have another go in moving a ship
                // the finished variable will check if the search was not terminated prematurely, if it was terminated
                // prematurely, give the score a very negative number, so this move will not be considered.
                if (remainingMovesAB == 1) {
                    var abResult = alphaBetaEval3MoveOrdering3TTNullID(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false, startTime)
                    var bestMoveScore = abResult["score"] as MutableList<Int>
                    returnedBoard = abResult["board"] as Board
                    result = bestMoveScore[0]
                    finished = abResult["finished"] as Boolean
                    if (!finished) result = -10000
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var abResult = alphaBetaEval3MoveOrdering3TTNullID(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false, startTime)
                        var bestMoveScore = abResult["score"] as MutableList<Int>
                        returnedBoard = abResult["board"] as Board
                        result = -bestMoveScore[0]
                        finished = abResult["finished"] as Boolean
                        if (!finished) result = -10000
                    } else if (playersTurn == "S") {
                        var abResult = alphaBetaEval3MoveOrdering3TTNullID(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false, startTime)
                        var bestMoveScore = abResult["score"] as MutableList<Int>
                        returnedBoard = abResult["board"] as Board
                        result = -bestMoveScore[0]
                        finished = abResult["finished"] as Boolean
                        if (!finished) result = -10000
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                    moveType = option.key
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) break@outerloop
                }
                boardCopy = boardCopy(boardInput)
            }
        }
    }
    // saving the move in the transposition table
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn, moveType)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable, "finished" to finished)
}


