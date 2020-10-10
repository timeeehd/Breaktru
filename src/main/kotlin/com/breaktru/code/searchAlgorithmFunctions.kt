package com.breaktru.code

import java.io.Serializable
import kotlin.random.Random

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

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("FA")
        return mutableMapOf("score" to mutableListOf(evaluatePiecesRandom(boardCopy, playersTurn)))
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0

    val possibleMoves = moveGenerator(boardCopy, playersTurn, remainingMoves)

    for (position in possibleMoves) {
//            println(position.key[0])
//            println(possibleMoves[position.key])
        for (move in possibleMoves[position.key]!!) {
            var result = Int.MIN_VALUE
            remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
            boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
//            boardCopy.print()
            if (remainingMovesAB == 1) {
//                    println(alphaBeta(depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"])
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
            }
            if (score > lowerbound) lowerbound = score
//            boardInput.print()
            boardCopy = boardCopy(boardInput)
//            boardInput.print()
            if (score >= upperbound) break
        }
    }
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo)
}

fun alphaBeta2(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("TEST")
        return Pair(mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn))), boardCopy)
    }

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)
//    var bestMove: MutableMap<String, MutableList<Int>>
//    var returnedBoard : Board

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)

    val possibleMoves = moveGenerator(boardCopy, playersTurn, remainingMoves)

    for (position in possibleMoves) {
//            println(position.key[0])
//            println(possibleMoves[position.key])
        for (move in possibleMoves[position.key]!!) {
            var result = Int.MIN_VALUE
            var returnedBoard = Board()
            remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
            boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
//            boardCopy.print()
            if (remainingMovesAB == 1) {
//                    println(alphaBeta(depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"])
                var test = alphaBeta2(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                var bestMove = test.first
                returnedBoard = test.second
                result = bestMove["score"]!![0]
//                println("from ${bestMove["from"]} to ${bestMove["to"]}")
            } else if (remainingMovesAB == 0) {
                if (playersTurn == "G") {
                    var test = alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                } else if (playersTurn == "S") {
                    var test = alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                }
            }

            if (result > score) {
                score = result
                moveFrom = position.key[0]
                moveTo = move
                bestBoard = boardCopy(returnedBoard)
//                bestBoard.print()
//                println("MoveFrom = $moveFrom")
//                println("moveTo = $moveTo")
            }
            if (score > lowerbound) lowerbound = score
//            boardInput.print()
            boardCopy = boardCopy(boardInput)
//            boardInput.print()
            if (score >= upperbound) {
                break
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta3(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Board> {

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("TEST")
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
//            println(option.key[0])
//            println(possibleMoves[position.key])
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
//            boardCopy.print()
                if (remainingMovesAB == 1) {
//                    println(alphaBeta(depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"])
                    var test = alphaBeta3(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test.first
                    returnedBoard = test.second
                    result = bestMove["score"]!![0]
//                println("from ${bestMove["from"]} to ${bestMove["to"]}")
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta3(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    } else if (playersTurn == "S") {
                        var test = alphaBeta3(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)
                        var bestMove = test.first
                        returnedBoard = test.second
                        result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
//                bestBoard.print()
//                println("MoveFrom = $moveFrom")
//                println("moveTo = $moveTo")
                }
                if (score > lowerbound) lowerbound = score
//            boardInput.print()
                boardCopy = boardCopy(boardInput)
//            boardInput.print()
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), bestBoard)
}

fun alphaBeta4(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): Pair<MutableMap<String, MutableList<Int>>, Pair<Board, MutableMap<Long, Map<String, Any>>>> {

    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)

    if (silverWin || goldWin || depth == 0) {
//        println("TEST")
        return Pair(mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn))), Pair(boardCopy, ttTable))
    }
    var flag = ""

    var score = Int.MIN_VALUE
    var upperbound = beta
    var lowerbound = alpha
    var moveFrom = mutableListOf(0, 0)
    var moveTo = mutableListOf(0, 0)

    var remainingMovesAB = 0
    var bestBoard = boardCopy(boardInput)

    val possibleMoves = moveGenerator2(boardCopy, playersTurn, remainingMoves)

    val olda = alpha
//    val ttRetrieval = retrieve(boardInput, ttTable)
//    if (ttRetrieval["depth"] as Int != -1) {
//                println("FUCK")
//            }
//    if (ttRetrieval["depth"] as Int >= depth) {
//        val retrievedFrom = ttRetrieval["from"] as MutableList<Int>
//        val retrievedTo = ttRetrieval["to"] as MutableList<Int>
//        val retrievedScore = ttRetrieval["score"] as Int
//        val retrievedPlayer = ttRetrieval["playersTurn"] as String
//        val calcScore = if(retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
//        if (ttRetrieval["flag"] as String == "Exact") {
//            return Pair(mutableMapOf("score" to mutableListOf(calcScore),
//                    "from" to retrievedFrom, "to" to retrievedTo), Pair(boardCopy, ttTable))
//        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
//            lowerbound = if (alpha >= calcScore) alpha else calcScore
//        } else if (ttRetrieval["flag"] as String == "Upperbound") {
//            upperbound = if (beta <= calcScore) beta else calcScore
//        }
//        if (lowerbound > upperbound) {
//            return Pair(mutableMapOf("score" to mutableListOf(calcScore),
//                    "from" to retrievedFrom, "to" to retrievedTo), Pair(boardCopy, ttTable))
//        }
//    }

    for (option in possibleMoves) {
//            println(option.key[0])
//            println(possibleMoves[position.key])
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
//            boardCopy.print()
                if (remainingMovesAB == 1) {
//                    println(alphaBeta(depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"])
                    var test = alphaBeta4(boardCopy, ttTable, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test.first
                    returnedBoard = test.second.first
//                    ttTable = test.second.second
                    result = bestMove["score"]!![0]
//                println("from ${bestMove["from"]} to ${bestMove["to"]}")
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta4(boardCopy, ttTable, depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test.first
                        returnedBoard = test.second.first
//                        ttTable = test.second.second
                        result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    } else if (playersTurn == "S") {
                        var test = alphaBeta4(boardCopy, table, depth - 1, -upperbound, -lowerbound, "G", 2)
                        var bestMove = test.first
                        returnedBoard = test.second.first
//                        ttTable = test.second.second
                        result = -bestMove["score"]!![0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
                    if (score > lowerbound) lowerbound = score
                    if (score >= upperbound) {
                        break
                    }
                }
//                boardInput.transpositionTable = boardCopy.transpositionTable
                boardCopy = boardCopy(boardInput)
            }
//            println(moveFrom)
        }
    }
//    var start = System.currentTimeMillis()
//    if (score <= olda) flag = "Upperbound"
//    else if (score >= beta) flag = "Lowerbound"
//    else flag = "Exact"
//    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
//    var timeSpent = System.currentTimeMillis() - start
//    println("Timespent storing: $timeSpent")
    return Pair(mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo), Pair(bestBoard, ttTable))
}

fun alphaBeta5(boardInput: Board, table: MutableMap<Long, Map<String, Any>>, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, Any> {

    var ttTable = table
    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("TEST")
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
    val ttRetrieval = retrieve(boardInput, ttTable)
    if (ttRetrieval["depth"] as Int != -1) {
                println("FUCK")
            }
    if (ttRetrieval["depth"] as Int >= depth) {
        val retrievedFrom = ttRetrieval["from"] as MutableList<Int>
        val retrievedTo = ttRetrieval["to"] as MutableList<Int>
        val retrievedScore = ttRetrieval["score"] as Int
        val retrievedPlayer = ttRetrieval["playersTurn"] as String
        val calcScore = if(retrievedPlayer == playersTurn) retrievedScore else -retrievedScore
        if (ttRetrieval["flag"] as String == "Exact") {
//            return Pair(mutableMapOf("score" to mutableListOf(calcScore),
//                    "from" to retrievedFrom, "to" to retrievedTo), Pair(boardCopy, ttTable))
        } else if (ttRetrieval["flag"] as String == "Lowerbound") {
            lowerbound = if (alpha >= calcScore) alpha else calcScore
        } else if (ttRetrieval["flag"] as String == "Upperbound") {
            upperbound = if (beta <= calcScore) beta else calcScore
        }
        if (lowerbound > upperbound) {
//            return Pair(mutableMapOf("score" to mutableListOf(calcScore),
//                    "from" to retrievedFrom, "to" to retrievedTo), Pair(boardCopy, ttTable))
        }
    }

    for (option in possibleMoves) {
//            println(option.key[0])
//            println(possibleMoves[position.key])
        for (position in possibleMoves[option.key]!!) {
            for (move in possibleMoves[option.key]!![position.key]!!) {
                var result = Int.MIN_VALUE
                var returnedBoard = Board()
                remainingMovesAB = calcRemainingMoves(boardCopy, position.key[0], move, remainingMoves)
                boardCopy.moveBackEnd(position.key[0], move, remainingMoves)
//            boardCopy.print()
                if (remainingMovesAB == 1) {
//                    println(alphaBeta(depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"])
                    var test = alphaBeta5(boardCopy, table,depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)
                    var bestMove = test["score"] as MutableList<Int>
                    returnedBoard = test["board"] as Board
                    result = bestMove[0]
//                println("from ${bestMove["from"]} to ${bestMove["to"]}")
                } else if (remainingMovesAB == 0) {
                    if (playersTurn == "G") {
                        var test = alphaBeta5(boardCopy, table,depth - 1, -upperbound, -lowerbound, "S", 2)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    } else if (playersTurn == "S") {
                        var test = alphaBeta5(boardCopy, table,depth - 1, -upperbound, -lowerbound, "G", 2)
                        var bestMove = test["score"] as MutableList<Int>
                        returnedBoard = test["board"] as Board
                        result = -bestMove[0]
//                    println("from ${bestMove["from"]} to ${bestMove["to"]}")
                    }
                }

                if (result > score) {
                    score = result
                    moveFrom = position.key[0]
                    moveTo = move
                    bestBoard = boardCopy(returnedBoard)
//                bestBoard.print()
//                println("MoveFrom = $moveFrom")
//                println("moveTo = $moveTo")
                }
                if (score > lowerbound) lowerbound = score
//            boardInput.print()
                boardCopy = boardCopy(boardInput)
//            boardInput.print()
                if (score >= upperbound) {
                    break
                }
            }
        }
    }
        var start = System.currentTimeMillis()
    if (score <= olda) flag = "Upperbound"
    else if (score >= beta) flag = "Lowerbound"
    else flag = "Exact"
    ttTable = store(boardInput, ttTable, moveFrom, moveTo, score, flag, depth, playersTurn)
    var timeSpent = System.currentTimeMillis() - start
    println("Timespent storing: $timeSpent")
    return mutableMapOf("score" to mutableListOf(score), "from" to moveFrom, "to" to moveTo, "board" to bestBoard, "tt" to ttTable)
}

//fun main() {
//
//    var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
//    var bestBoard = Board()
//
//    val board = Board();
//    board.initialize()
//    val board2 = Board()
//    board2.initialize()
////    var abResult = alphaBeta3(board, 3, -10000, 10000, "G", 2).first
////    println(abResult)
//    var start = System.currentTimeMillis()
//    var depth = 1
//    var timeSpent = 0L
//    while (depth  < 4) {
////            var returnedValue = alphaBeta3(board,depth, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
//        var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, "G", 2)
//        abResult = returnedValue.first
//        bestBoard = returnedValue.second.first
////            bestBoard = returnedValue.second
//        bestBoard.print()
//        board.transpositionTable = returnedValue.second.second
//        timeSpent = System.currentTimeMillis() - start
//        println("Depth: $depth")
//        println("Timespent: $timeSpent")
//        depth++
//    }
//
//}

