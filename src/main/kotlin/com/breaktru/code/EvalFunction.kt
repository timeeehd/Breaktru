package com.breaktru.code

import kotlin.random.Random

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
    val pieces = evaluatePieces(board, playersTurn)
    return pieces + rand(-10, 10)
}

fun evaluate(board: Board, playersTurn: String): Int {
//    println("TEST")
    val (silverWin, goldWin) = terminalNode(board)
    if (silverWin && playersTurn == "S") {
//        println("Silver Win")
        return 100000
    }
    if (silverWin && playersTurn == "G") {
//        println("Silver Win")
        return -100000
    }
    if (goldWin && playersTurn == "G") {
//        println("Gold Win")
        return 100000
    }
    if (goldWin && playersTurn == "S") {
//        println("Gold Win")
        return -100000
    }

//    if(goldWin && playersTurn == "S") return -10000
//    if(silverWin && playersTurn == "G") return -10000
    val pieces = evaluatePieces(board, playersTurn)
    val (silverPos, goldPos) = evaluatePositionBoard(board, playersTurn)
    val posGFS = evaluatePositionToGFS(board, playersTurn)
//    board.print()
    var sendBack = 0
//    board.print()
//    if(board.board[3][3].color == "S" && board.board[3][7].color == "S"){
//        println("HOI")
//    }
    if (playersTurn == "G") sendBack = (21 * pieces + 2 * (goldPos - silverPos) + 1 * posGFS)
    if (playersTurn == "S") sendBack = (21 * pieces + 2 * (silverPos - goldPos) + 1 * posGFS)
//    if (sendBack < 0) return rand(sendBack/20, -sendBack/20)
    return sendBack
}

fun evaluatePieces(board: Board, playersTurn: String): Int {
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
//    if (playersTurn == "G") {
//        value = -value
//    }
//    if (value == 40) {
//        println("HOI")
//    }

//    val random = rand(-10,10)

//    println(random)

    return value
}

fun evaluatePositionBoard(board: Board, playersTurn: String): Pair<Int, Int> {
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

fun evaluatePositionToGFS(board: Board, playersTurn: String): Int {
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
//                    board.print()
//                    println(row)
//                    println(col)
                    if (board.board[row - 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row + 1][col].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col - 1].color == "S") {
                        silverEval += 10
                    }
                    if (board.board[row][col + 1].color == "S") {
                        silverEval += 10
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

fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}
