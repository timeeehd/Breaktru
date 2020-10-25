package com.breaktru.code

// Old alpha beta functions that are code cleaned

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
    outloop@ for (position in possibleMoves) {
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
                if (score >= upperbound) {
                    break@outloop
                }
            }
            boardCopy = boardCopy(boardInput)
        }
    }
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo)
}

fun alphaBetaEval(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {

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
                var test = alphaBetaEval(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                var bestMove = test.first
                returnedBoard = test.second
                result = bestMove["score"]!![0]
            } else if (remainingMovesAB == 0) {
                if (playersTurn == "G") {
                    var test = alphaBetaEval(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = -bestMove["score"]!![0]
                } else if (playersTurn == "S") {
                    var test = alphaBetaEval(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
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

fun alphaBetaEvalMoveOrdering(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {
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

    outerloop@for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBetaEvalMoveOrdering(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEvalMoveOrdering(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEvalMoveOrdering(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
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
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
                        break@outerloop
                    }
                }
                boardCopy = boardCopy(boardInput)
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBetaEvalMoveOrderingTT(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, Any> {
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
    var moveType: String = ""

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

    outerloop@for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBetaEvalMoveOrderingTT(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEvalMoveOrderingTT(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEvalMoveOrderingTT(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2)
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
                    moveType = option.key
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
//                    count2++
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
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn, moveType)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBetaEvalMoveOrderingNull(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): Pair<MutableMap<String, MutableList<Int>>, Board> {
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
        if (playersTurn == "G") {
            var test = alphaBetaEvalMoveOrderingNull(boardCopy, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        } else if (playersTurn == "S") {
            var test = alphaBetaEvalMoveOrderingNull(boardCopy, depth - 2, -upperbound, -lowerbound, "G", 2, false)
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
                    var test = alphaBetaEvalMoveOrderingNull(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEvalMoveOrderingNull(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEvalMoveOrderingNull(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2, false)
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

fun alphaBetaEvalMoveOrderingTTNull(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {
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
    var moveType: String = ""

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
        if (playersTurn == "G") {
            var test = alphaBetaEvalMoveOrderingTTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBetaEvalMoveOrderingTTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable)
        }
    }
    score = Int.MIN_VALUE

    outerloop@ for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBetaEvalMoveOrderingTTNull(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEvalMoveOrderingTTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEvalMoveOrderingTTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
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
                    moveType = option.key
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
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn, moveType)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBetaEval2MoveOrderingTTNull(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(evaluateWithRandom(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
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
    var moveType: String = ""

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
        if (playersTurn == "G") {
            var test = alphaBetaEval2MoveOrderingTTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, false)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBetaEval2MoveOrderingTTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, false)
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
                    var test = alphaBetaEval2MoveOrderingTTNull(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEval2MoveOrderingTTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEval2MoveOrderingTTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
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
                    moveType = option.key
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
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn, moveType)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

fun alphaBetaEval2MoveOrderingNull(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): Pair<MutableMap<String, MutableList<Int>>, Board> {
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return Pair(mutableMapOf("score" to mutableListOf(evaluateWithRandom(boardCopy, playersTurn))), boardCopy)
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
        if (playersTurn == "G") {
            var test = alphaBetaEval2MoveOrderingNull(boardCopy, depth - 2, -upperbound, -lowerbound, "S", 2, true)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        } else if (playersTurn == "S") {
            var test = alphaBetaEval2MoveOrderingNull(boardCopy, depth - 2, -upperbound, -lowerbound, "G", 2, true)
            var bestMove = test.first
            returnedBoard = test.second
            result = -bestMove["score"]!![0]
        }
        if (result >= upperbound) {
            return Pair(mutableMapOf("score" to mutableListOf((upperbound))), boardCopy)
        }
    }
    score = Int.MIN_VALUE
    outerloop@for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                result = Int.MIN_VALUE
                returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBetaEval2MoveOrderingNull(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEval2MoveOrderingNull(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEval2MoveOrderingNull(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2, false)
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
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
                        break@outerloop
                    }
                }

                boardCopy = boardCopy(boardInput)

            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBetaEval3MoveOrdering3TTNull(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int, rootNode: Boolean): MutableMap<String, Any> {
    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
        return mutableMapOf("score" to mutableListOf(finalEvaluation(boardCopy, playersTurn)), "board" to boardCopy, "tt" to ttTable)
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
    var retrievedScore: Int = -10000
    var calcScore = -10000
    var moveType: String = ""

    val olda = alpha
    val ttRetrieval = retrieve(boardInput, ttTable, playersTurn)

    if (ttRetrieval["depth"] as Int >= 0) {
        retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        retrievedTo = ttRetrieval["to"] as MutableList<Int>
        retrievedScore = ttRetrieval["score"] as Int

        var retrievedPlayer = ttRetrieval["playersTurn"] as String
        var retrievedMoveType = ttRetrieval["movetype"] as String
        calcScore = if (retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (remainingMoves == 1 && (retrievedMoveType == "capture" || retrievedMoveType == "flagship")) {
            retrievedFrom = mutableListOf()
            retrievedTo = mutableListOf()
        }
    }

    if (ttRetrieval["depth"] as Int >= depth) {
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
        if (playersTurn == "G") {
            var test = alphaBetaEval3MoveOrdering3TTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "S", 2, true)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        } else if (playersTurn == "S") {
            var test = alphaBetaEval3MoveOrdering3TTNull(boardCopy, table, depth - 2, -upperbound, -lowerbound, "G", 2, true)
            var bestMove = test["score"] as MutableList<Int>
            returnedBoard = test["board"] as Board
            result = -bestMove[0]
        }
        if (result >= upperbound) {
            return mutableMapOf("score" to mutableListOf(upperbound), "board" to boardCopy, "tt" to ttTable)
        }
    }
    score = Int.MIN_VALUE
    val possibleMoves = finalMoveGeneration(boardCopy, playersTurn, remainingMoves, retrievedFrom, retrievedTo)

//    println("at depth $depth the moves are ${possibleMoves["moves"]}")
//    println("at depth $depth the flagshipmoves are ${possibleMoves["flagship"]}")
    outerloop@ for (option in possibleMoves) {
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
                if (remainingMovesAB == 1) {
                    var test = alphaBetaEval3MoveOrdering3TTNull(boardCopy, table, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB, false)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBetaEval3MoveOrdering3TTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "S", 2, false)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
                    } else if (playersTurn == "S") {
                        var test = alphaBetaEval3MoveOrdering3TTNull(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2, false)
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
                    moveType = option.key
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

    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn, moveType)
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}
