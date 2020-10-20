package com.breaktru.code

var count2 = 0
var count3 = 0

val transpositionTable = mutableMapOf<Long, Map<String, Any>>()

val GFSPos: Array<IntArray> = arrayOf(intArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000),
        intArrayOf(1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 1000),
        intArrayOf(1000, 100, 50, 50, 50, 50, 50, 50, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 25, 25, 25, 25, 25, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 25, 20, 20, 20, 25, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 25, 20, 0, 20, 25, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 25, 20, 20, 20, 25, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 25, 25, 25, 25, 25, 50, 100, 1000),
        intArrayOf(1000, 100, 50, 50, 50, 50, 50, 50, 50, 100, 1000),
        intArrayOf(1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 1000),
        intArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000))

val SEPos: Array<IntArray> = arrayOf(intArrayOf(-10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10),
        intArrayOf(-10, 1, 1, 1, 1, 1, 1, 1, 1, 1, -10),
        intArrayOf(-10, 1, 5, 5, 5, 5, 5, 5, 5, 1, -10),
        intArrayOf(-10, 1, 5, 25, 25, 25, 25, 25, 5, 1, -10),
        intArrayOf(-10, 1, 5, 25, 50, 50, 50, 25, 5, 1, -10),
        intArrayOf(-10, 1, 5, 25, 50, 100, 50, 25, 5, 1, -10),
        intArrayOf(-10, 1, 5, 25, 50, 50, 50, 25, 5, 1, -10),
        intArrayOf(-10, 1, 5, 25, 25, 25, 25, 25, 5, 1, -10),
        intArrayOf(-10, 1, 5, 5, 5, 5, 5, 5, 5, 1, -10),
        intArrayOf(-10, 1, 1, 1, 1, 1, 1, 1, 1, 1, -10),
        intArrayOf(-10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10))


fun alphaBeta(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, MutableList<Int>> {
    var start = System.currentTimeMillis()
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluatePiecesRandom(boardCopy, playersTurn)))
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0

    val possibleMoves = moveGenerator(boardCopy, playersTurn, remainingMoves)
    outloop@for (position in possibleMoves) {
        for (move in possibleMoves[position.key]!!) {
            var result = Int.MIN_VALUE

            remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
            boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
            if (remainingMovesAB == 1) {
                result = alphaBeta(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"]!![0]
            } else if (remainingMovesAB == 0) {
                if (playersTurn == "G") {
                    result = -alphaBeta(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)["score"]!![0]
                } else if (playersTurn == "S") {
                    result = -alphaBeta(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)["score"]!![0]
                }
            }

            if (result > score) {
                score = result
                moveFrom = position.key[0]
                moveTo = move
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
                    break@outloop
                }
            }
        }
    }
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo)
}

fun alphaBeta2(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return Pair(mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn))), boardCopy)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)
    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)

    val possibleMoves = moveGenerator(boardCopy, playersTurn, remainingMoves)

    for (position in possibleMoves) {
        for (move in possibleMoves[position.key]!!) {
            var result = Int.MIN_VALUE
            var returnedBoard = Board()
            remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
            boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
            if (remainingMovesAB == 1) {
                var test = alphaBeta2(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                var bestMove = test.first
                returnedBoard = test.second
                result = bestMove["score"]!![0]
            } else if (remainingMovesAB == 0) {
                if (playersTurn == "G") {
                    var test = alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = -bestMove["score"]!![0]
                } else if (playersTurn == "S") {
                    var test = alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = -bestMove["score"]!![0]
                }
            }

            if (result > score) {
                score = result
                moveFrom = position.key[0]
                moveTo = move
                bestBoard = boardCopy(returnedBoard)
            }
            if (score > lowerbound) lowerbound = score
            boardCopy = boardCopy(boardInput)
            if (score >= upperbound) {
                break
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta3(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {

    count2++
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return Pair(mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn))), boardCopy)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)

    for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta3(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta3(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta3(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                }
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta4(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, Any> {

    count2++
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)
    var flag = ""
    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)

    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)
    if (ttRetrieval["depth"] as Int >= depth) {
        val retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        val retrievedTo = ttRetrieval["to"] as MutableList<Int>
        val retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (ttRetrieval["flag"] as String == "Exact") {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        }
    }

    for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta4(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta4(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta4(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                }
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
//                    count2++
                    break
                }
            }
        }
    }
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBeta5(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): Pair<MutableMap<String, MutableList<Int>>, Board> {

    count2++
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return Pair(mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn))), boardCopy)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)
    var returnedBoard = Board()
    var result = Int.MIN_VALUE

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)
    if (depth >= 2 && !rootNode) {
        count3++
        if (playersTurn == "G") {
            var test = alphaBeta5(boardCopy, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        } else if (playersTurn == "S") {
            var test = alphaBeta5(boardCopy, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        }
        if (result >= upperbound) {
            return Pair(mutableMapOf("score" to mutableListOf((upperbound))), boardCopy)
        }
    }
    score = Int.MIN_VALUE
    for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                result = Int.MIN_VALUE
                returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta5(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta5(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta5(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                }
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta6(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {

    count2++
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
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

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)

    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)
    if (ttRetrieval["depth"] as Int >= depth) {
        val retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        val retrievedTo = ttRetrieval["to"] as MutableList<Int>
        val retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (ttRetrieval["flag"] as String == "Exact") {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        }
    }

    if (depth >= 2 && !rootNode) {
        count3++
        if (playersTurn == "G") {
            var test = alphaBeta6(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBeta6(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable)
        }
    }
    score = Int.MIN_VALUE

    outerloop@for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta6(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta6(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta6(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
                        break@outerloop
                    }
                }
                boardCopy = boardCopy(boardInput)
            }
        }
    }
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBeta7(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {

    count2++
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluate2(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
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

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)

    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)
    if (ttRetrieval["depth"] as Int >= depth) {
        val retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        val retrievedTo = ttRetrieval["to"] as MutableList<Int>
        val retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (ttRetrieval["flag"] as String == "Exact") {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        }
    }

    if (depth >= 2 && !rootNode) {
        count3++
        if (playersTurn == "G") {
            var test = alphaBeta7(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBeta7(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable)
        }
    }
    score = Int.MIN_VALUE

    for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta7(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta7(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta7(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                }
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBeta8(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): Pair<MutableMap<String, MutableList<Int>>, Board> {

    count2++
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return Pair(mutableMapOf("score" to mutableListOf(evaluate2(boardCopy, playersTurn))), boardCopy)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)
    var returnedBoard = Board()
    var result = Int.MIN_VALUE

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)
    if (depth >= 2 && !rootNode) {
        count3++
        if (playersTurn == "G") {
            var test = alphaBeta8(boardCopy, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        } else if (playersTurn == "S") {
            var test = alphaBeta8(boardCopy, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        }
        if (result >= upperbound) {
            return Pair(mutableMapOf("score" to mutableListOf((upperbound))), boardCopy)
        }
    }
    score = Int.MIN_VALUE
    for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                result = Int.MIN_VALUE
                returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta8(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta8(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta8(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                }
                if (score > lowerbound) lowerbound = score
                boardCopy = boardCopy(boardInput)
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta9(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {

    count2++
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluate3(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
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

    var retrievedFrom: MutableList<Int> = mutableListOf()
    var retrievedTo: MutableList<Int> = mutableListOf()

    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)
    if (ttRetrieval["depth"] as Int >= depth) {
        retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        retrievedTo = ttRetrieval["to"] as MutableList<Int>
        val retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (ttRetrieval["flag"] as String == "Exact") {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
            return mutableMapOf("score" to mutableListOf(calcScore),
                    "from" to retrievedFrom, "to" to retrievedTo, "board" to boardCopy, "tt" to ttTable)
        }
    }

    if (depth >= 2 && !rootNode) {
        count3++
        if (playersTurn == "G") {
            var test = alphaBeta9(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBeta9(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable)
        }
    }
    score = Int.MIN_VALUE
    val possibleMoves = moveGenerator3(boardCopy, playersTurn, remainingMoves, retrievedFrom, retrievedTo)


    outerloop@for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBeta9(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta9(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBeta9(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
                        break@outerloop
                    }
                }
                boardCopy = boardCopy(boardInput)
            }
        }
    }
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}


//fun main() {
////    val start = System.currentTimeMillis()
////    val board3 = Board()
////    board3.initialize()
////    var board4 = Board()
////    println("Init ${System.currentTimeMillis() - start}")
////    board4 = boardCopy(board3)
////    println(System.currentTimeMillis() - start)
////      alphaBeta(board3, 3, -10000, 10000, "S", 2)
////    println(System.currentTimeMillis() - start)
////    var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
////    var bestBoard = Board()
////
//    val board = Board();
//    board.initialize()
//    val board2 = Board()
//    board2.initialize()
//    val board3 = Board();
//    board3.initialize()
//    val board4 = Board()
//    board4.initialize()
//    val board5 = Board()
//    board5.initialize()
//    val board6 = Board()
//    board6.initialize()
////
//////    alphaBeta5(board, 2, -10000, 10000, "G", 2)
//////    println(abResult)
////    var start = System.currentTimeMillis()
////    var depth = 1
////    var timeSpent = 0L
////    count2 = 0
////    start = System.currentTimeMillis()
//////    alphaBeta5(board, 1, -10000, 10000, "G", 2)
////    depth = 1
//////
////////    alphaBeta5(board, board.transpositionTable, 3, -10000, 10000, "G", 2)
//////    println("count2 $count2")
////
//////    count2 = 0
//////    println("count2 $count2")
////    start = System.currentTimeMillis()
////    timeSpent = 0L
////    depth = 1
////    println("Normal Alpha Beta")
////    while (depth < 5) {
////        count2 = 0
////        count3 = 0
////        alphaBeta3(board3, depth, -10000, 10000, "S", 2)
////        timeSpent = System.currentTimeMillis() - start
////        println("depth $depth")
////        depth++
////        println("count2 $count2")
////        println("count3 $count3")
////    }
////    println("count2 $count2")
////    println("Timespent: $timeSpent")
////    println()
////    println("Alpha beta with Null")
////    count2 = 0
////    depth = 1
////    start = System.currentTimeMillis()
////    timeSpent = 0L
////    while (depth < 5) {
////        count2 = 0
////        count3 = 0
////        alphaBeta6(board4, depth, -10000, 10000, "S", 2, true)
////        timeSpent = System.currentTimeMillis() - start
////        println("depth Null $depth")
////        depth++
////        println("count2 $count2")
////        println("count3 $count3")
////    }
////    println("count2 $count2")
////    println("count3 $count3")
////    println("Timespent: $timeSpent")
////    println()
////    println("Alpha beta with Null Random eval")
////    count2 = 0
////    depth = 1
////    start = System.currentTimeMillis()
////    timeSpent = 0L
////    while (depth < 5) {
////        count2 = 0
////        count3 = 0
////        alphaBeta9(board5, depth, -10000, 10000, "S", 2, true)
////        timeSpent = System.currentTimeMillis() - start
////        println("depth Null $depth")
////        depth++
////        println("count2 $count2")
////        println("count3 $count3")
////    }
////    println("count2 $count2")
////    println("count3 $count3")
////    println("Timespent: $timeSpent")
////    println()
////    println("Transposition Table")
////    count2 = 0
////    depth = 1
////    start = System.currentTimeMillis()
////    timeSpent = 0L
////    while (depth < 5) {
////        count3 = 0
////        count2 = 0
////        val result = alphaBeta4(board, board.transpositionTable, depth, -10000, 10000, "S", 2)
////        board.transpositionTable = result["tt"] as MutableMap<Long, Map<String, Any>>
////        println("depth TT $depth")
////        depth++
////        timeSpent = System.currentTimeMillis() - start
////        println("count2 $count2")
////        println("count3 $count3")
////    }
////    println("Timespent: $timeSpent")
////    println()
////    println()
//    println("Transposition Table with Null")
//    var start = System.currentTimeMillis()
//    var timeSpent = 0L
//    var depth = 1
//    while (depth < 4) {
//        count3 = 0
//        count2 = 0
//        val result = alphaBeta10(board2, board2.transpositionTable, depth, -10000, 10000, "S", 2, true)
//        board2.transpositionTable = result["tt"] as MutableMap<Long, Map<String, Any>>
//        println("depth TT Null $depth")
//        depth++
//        timeSpent = System.currentTimeMillis() - start
//        println("count2 $count2")
//        println("count3 $count3")
//    }
//    println("Timespent: $timeSpent")
////    println()
////    println()
////    println("Transposition Table with Null with Random")
////    start = System.currentTimeMillis()
////    timeSpent = 0L
////    depth = 1
////    while (depth < 5) {
////        count3 = 0
////        count2 = 0
////        val result = alphaBeta8(board6, board6.transpositionTable, depth, -10000, 10000, "S", 2, true)
////        board6.transpositionTable = result["tt"] as MutableMap<Long, Map<String, Any>>
////        println("depth TT Null $depth")
////        depth++
////        timeSpent = System.currentTimeMillis() - start
////        println("count2 $count2")
////        println("count3 $count3")
////    }
////    println("Timespent: $timeSpent")
////    println()
//}

