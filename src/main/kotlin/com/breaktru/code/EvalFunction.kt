package com.breaktru.code

import kotlin.random.Random

// the functions with help in front are actually the features I'm evaluating

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

// The function I used in my final version
fun finalEvaluation(board: Board, playersTurn: String): Int {
    val (silverWin, goldWin) = terminalNode(board)
    if (silverWin && playersTurn == "S") {
        return 100000
    }
    if (silverWin && playersTurn == "G") {
        return -100000
    }
    if (goldWin && playersTurn == "G") {
        return 100000
    }
    if (goldWin && playersTurn == "S") {
        return -100000
    }

    val pieces = helpEvaluatePieces(board, playersTurn)
    val (silverPos, goldPos) = helpEvaluatePositionBoard(board, playersTurn)
    val posGFS = helpEvaluatePositionToGFS(board, playersTurn)
    val freePass = helpEvaluateFreePassFS(board)
    var sendBack = 0
    if (playersTurn == "G") sendBack = (21 * pieces + 2 * (goldPos - silverPos) + 1 * posGFS + freePass)
    if (playersTurn == "S") sendBack = (21 * pieces + 2 * (silverPos - goldPos) + 1 * posGFS - freePass)

    return sendBack
}

fun terminalNode(board: Board): Pair<Boolean, Boolean> {
    var silverWin = true
    var goldWin = false
    for (row in 0..10) {
        for (col in 0..10) {
            if (board.board[row][col].name == "FS") {
                silverWin = false
                if (row == 0 || row == 10 || col == 0 || col == 10) {
                    goldWin = true
                    return Pair(silverWin, goldWin)
                }
            }
        }
    }
    return Pair(silverWin, goldWin)
}

fun evaluatePiecesRandom(board: Board, playersTurn: String): Int {
    val pieces = helpEvaluatePieces(board, playersTurn)
    return pieces + rand(-10, 10)
}

fun evaluate(board: Board, playersTurn: String): Int {
    val (silverWin, goldWin) = terminalNode(board)
    if (silverWin && playersTurn == "S") {
        return 100000
    }
    if (silverWin && playersTurn == "G") {
        return -100000
    }
    if (goldWin && playersTurn == "G") {
        return 100000
    }
    if (goldWin && playersTurn == "S") {
        return -100000
    }

    val pieces = helpEvaluatePieces(board, playersTurn)
    val (silverPos, goldPos) = helpEvaluatePositionBoard(board, playersTurn)
    val posGFS = helpEvaluatePositionToGFS(board, playersTurn)
    var sendBack = 0
    if (playersTurn == "G") sendBack = (21 * pieces + 2 * (goldPos - silverPos) + 1 * posGFS)
    if (playersTurn == "S") sendBack = (21 * pieces + 2 * (silverPos - goldPos) + 1 * posGFS)
    return sendBack
}

fun evaluateWithRandom(board: Board, playersTurn: String): Int {
    val (silverWin, goldWin) = terminalNode(board)
    if (silverWin && playersTurn == "S") {
        return 100000
    }
    if (silverWin && playersTurn == "G") {
        return -100000
    }
    if (goldWin && playersTurn == "G") {
        return 100000
    }
    if (goldWin && playersTurn == "S") {
        return -100000
    }

    val pieces = helpEvaluatePieces(board, playersTurn)
    val (silverPos, goldPos) = helpEvaluatePositionBoard(board, playersTurn)
    val posGFS = helpEvaluatePositionToGFS(board, playersTurn)
    var sendBack = 0
    var rand = 0
    if (playersTurn == "G") sendBack = (21 * pieces + 2 * (goldPos - silverPos) + 1 * posGFS)
    if (playersTurn == "S") sendBack = (21 * pieces + 2 * (silverPos - goldPos) + 1 * posGFS)
    if (sendBack < 0) rand = rand(sendBack/20, -sendBack/20)
    else rand = rand(-sendBack/20, sendBack/20)
    sendBack += rand
    return sendBack
}

fun helpEvaluatePieces(board: Board, playersTurn: String): Int {
    var value = 0
    if (playersTurn == "G") {
        for (row in 0..10) {
            for (col in 0..10) {
                if (board.board[row][col].color == "S") {
                    value -= 10
                } else if (board.board[row][col].color == "G" && board.board[row][col].name == "FS") {
                    value += 80
                } else if (board.board[row][col].color == "G") {
                    value += 10
                }
            }
        }
    } else if (playersTurn == "S") {
        for (row in 0..10) {
            for (col in 0..10) {
                if (board.board[row][col].color == "S") {
                    value += 10
                } else if (board.board[row][col].color == "G" && board.board[row][col].name == "FS") {
                    value -= 80
                } else if (board.board[row][col].color == "G") {
                    value -= 10
                }
            }
        }
    }

    val (silverWin, goldWin) = terminalNode(board)
    if (silverWin) {
        value += 100000
    } else if (goldWin) {
        value -= -100000
    }
    return value
}

fun helpEvaluatePositionBoard(board: Board, playersTurn: String): Pair<Int, Int> {
    var silverEval = 0
    var GFSEval = 0
    for (row in 0..10) {
        for (col in 0..10) {
            if (board.board[row][col].color == "S") {
                silverEval += SEPos[row][col]
            }
            if (board.board[row][col].name == "FS") {
                GFSEval = GFSPos[row][col]
            }
        }
    }
    return Pair(silverEval, GFSEval)
}

fun helpEvaluatePositionToGFS(board: Board, playersTurn: String): Int {
    var silverEval = 0
    var goldEval = 0
    for (row in 0..10) {
        for (col in 0..10) {
            if (board.board[row][col].name == "FS") {
                if (row == 0 && col == 0) {
                    if (board.board[row + 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col + 1].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row + 1][col + 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row + 1][col].color == "G") {
                        goldEval += 50
                    }
                    if (board.board[row][col + 1].color == "G") {
                        goldEval += 50
                    }
//                    if (board.board[row + 1][col + 1].color == "G") {
//                        goldEval += 100
//                    }
                } else if (row == 0 && col == 10) {
                    if (board.board[row + 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col - 1].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row + 1][col - 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row + 1][col].color == "G") {
                        goldEval += 50
                    }
                    if (board.board[row][col - 1].color == "G") {
                        goldEval += 50
                    }
//                    if (board.board[row + 1][col - 1].color == "G") {
//                        goldEval += 100
//                    }
                } else if (row == 10 && col == 0) {
                    if (board.board[row - 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col + 1].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row - 1][col + 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row - 1][col].color == "G") {
                        goldEval += 50
                    }
                    if (board.board[row][col + 1].color == "G") {
                        goldEval += 50
                    }
//                    if (board.board[row - 1][col + 1].color == "G") {
//                        goldEval += 100
//                    }
                } else if (row == 10 && col == 10) {
                    if (board.board[row - 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col - 1].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row - 1][col - 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row - 1][col].color == "G") {
                        goldEval += 50
                    }
                    if (board.board[row][col - 1].color == "G") {
                        goldEval += 50
                    }
//                    if (board.board[row - 1][col - 1].color == "G") {
//                        goldEval += 100
//                    }
                } else {
                    if (board.board[row - 1][col].color == "S") {
                        silverEval += 25
                    }
                    if (board.board[row + 1][col].color == "S") {
                        silverEval += 25
                    }
                    if (board.board[row][col - 1].color == "S") {
                        silverEval += 25
                    }
                    if (board.board[row][col + 1].color == "S") {
                        silverEval += 25
                    }
                    if (board.board[row - 1][col - 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row + 1][col + 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row - 1][col + 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row + 1][col - 1].color == "S") {
                        silverEval += 50
                    }
                    if (board.board[row - 1][col].color == "G") {
                        goldEval += -10
                    }
                    if (board.board[row + 1][col].color == "G") {
                        goldEval += -10
                    }
                    if (board.board[row][col - 1].color == "G") {
                        goldEval += -10
                    }
                    if (board.board[row][col + 1].color == "G") {
                        goldEval += -10
                    }
                    if (board.board[row - 1][col - 1].color == "G") {
                        goldEval += 20
                    }
                    if (board.board[row + 1][col + 1].color == "G") {
                        goldEval += 20
                    }
                    if (board.board[row - 1][col + 1].color == "G") {
                        goldEval += 20
                    }
                    if (board.board[row + 1][col - 1].color == "G") {
                        goldEval += 20
                    }
                }
            }
        }
    }
    if (playersTurn == "S") return silverEval - goldEval
    if (playersTurn == "G") return goldEval - silverEval
    return 0
}

fun helpEvaluateFreePassFS(board: Board) : Int{
    var scoreDown = 1000
    var scoreUp = 1000
    var scoreLeft = 1000
    var scoreRight = 1000
    for (row in 0..10) {
        for (col in 0..10) {
            if (board.board[row][col].name == "FS") {
                for (down in row downTo 0) {
                    if (board.board[down][col].name != "  ") {
                        scoreDown = 0
                        break
                    }
                }
                for (up in row .. 10) {
                    if (board.board[up][col].name != "  ") {
                        scoreUp = 0
                        break
                    }
                }
                for (left in col downTo 0) {
                    if (board.board[row][left].name != "  ") {
                        scoreLeft = 0
                        break
                    }
                }
                for (right in col .. 10) {
                    if (board.board[row][right].name != "  ") {
                        scoreRight = 0
                        break
                    }
                }

            }
        }
    }
    return scoreDown + scoreLeft + scoreUp + scoreRight
}

fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}
