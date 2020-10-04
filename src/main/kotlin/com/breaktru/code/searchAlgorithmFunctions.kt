package com.breaktru.code

import kotlin.random.Random

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

val SEPos: Array<IntArray> = arrayOf(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0),
        intArrayOf(0, 1, 5, 5, 5, 5, 5, 5, 5, 1, 0),
        intArrayOf(0, 1, 5, 25, 25, 25, 25, 25, 5, 1, 0),
        intArrayOf(0, 1, 5, 25, 50, 50, 50, 25, 5, 1, 0),
        intArrayOf(0, 1, 5, 25, 50, 100, 50, 25, 5, 1, 0),
        intArrayOf(0, 1, 5, 25, 50, 50, 50, 25, 5, 1, 0),
        intArrayOf(0, 1, 5, 25, 25, 25, 25, 25, 5, 1, 0),
        intArrayOf(0, 1, 5, 5, 5, 5, 5, 5, 5, 1, 0),
        intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

fun moveGenerator(board: Board, color: String, remainingMoves: Int): MutableMap<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>> {
//        println("remaining moves ${remainingMoves}")
    var possibleMoves: MutableList<MutableList<Int>> = ArrayList()
    val testMap = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    for (row in 0..10) {
        col@ for (col in 0..10) {
            possibleMoves = ArrayList()
            var currentPostion: MutableList<MutableList<Int>> = ArrayList()
            if (board.board[row][col].color == color) {
                if (board.board[row][col].id != board.lastMove) {
                    if (remainingMoves == 1 && board.board[row][col].name == "FS") {
//                        println("Hier mag ik niet komen")
                        continue@col
                    }
                    if (remainingMoves != 1) {
                        if (row == 0 && col == 0) {

                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row + 1, col + 1))
                            }
                        } else if (row == 0 && col == 10) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                possibleMoves.add(mutableListOf(row + 1, col - 1))
                            }
                        } else if (row == 0) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                possibleMoves.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row + 1, col + 1))
                            }

                        } else if (row == 10 && col == 0) {

                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (row == 10 && col == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col - 1))
                            }
                        } else if (row == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 0) {

                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row + 1, col + 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 10) {

                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                possibleMoves.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col - 1))
                            }

                        } else {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row - 1, col + 1))
                            }
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                possibleMoves.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                possibleMoves.add(mutableListOf(row + 1, col + 1))
                            }
                        }
                    }

                    for (up in row - 1 downTo 0) {
                        if (board.board[up][col].color != " ") {
                            break
                        } else {
                            possibleMoves.add(mutableListOf(up, col))
                        }
                    }
                    for (down in (row + 1)..10) {
                        if (board.board[down][col].color != " ") {
                            break
                        } else {
                            possibleMoves.add(mutableListOf(down, col))
                        }
                    }
                    for (left in col - 1 downTo 0) {
                        if (board.board[row][left].color != " ") {
                            break
                        } else {
                            possibleMoves.add(mutableListOf(row, left))
                        }
                    }
                    for (right in (col + 1)..10) {
                        if (board.board[row][right].color != " ") {
                            break
                        } else {
                            possibleMoves.add(mutableListOf(row, right))
                        }
                    }
                }
            }
            if (possibleMoves.isNotEmpty()) {
                currentPostion.add(mutableListOf(row, col))
                testMap[currentPostion] = possibleMoves
            }

        }

    }
//    println(testMap)
    return testMap
}

fun alphaBeta(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, MutableList<Int>> {

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("FA")
        return mutableMapOf("score" to mutableListOf(evaluatePieces(boardCopy, playersTurn)))
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

fun alphaBeta2(boardInput: Board, depth: Int, alpha: Int, beta: Int, playersTurn: String, remainingMoves: Int): MutableMap<String, MutableList<Int>> {

    var boardCopy = boardCopy(boardInput)
    val (silverWin, goldWin) = terminalNode(boardCopy)
    if (silverWin || goldWin || depth == 0) {
//        println("TEST")
        return mutableMapOf("score" to mutableListOf(evaluate(boardCopy, playersTurn)))
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
                result = alphaBeta2(boardCopy, depth - 1, lowerbound, upperbound, playersTurn, remainingMovesAB)["score"]!![0]
            } else if (remainingMovesAB == 0) {
                if (playersTurn == "G") {
                    result = -alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "S", 2)["score"]!![0]
                } else if (playersTurn == "S") {
                    result = -alphaBeta2(boardCopy, depth - 1, -upperbound, -lowerbound, "G", 2)["score"]!![0]
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


fun boardCopy(board: Board): Board {
    val boardCopy = Board()
    for (row in 0..10) {
        for (col in 0..10) {
            boardCopy.board[row][col] = board.board[row][col]
        }
    }
    boardCopy.lastMove = board.lastMove
    return boardCopy
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

fun evaluate(board: Board, playersTurn: String): Int {
//    println("TEST")
    val (silverWin, goldWin) = terminalNode(board)
    if(silverWin && playersTurn == "S"){
        println("Silver Win")
        return 100000
    }
    if(silverWin && playersTurn == "G"){
        println("Silver Win")
        return -100000
    }
    if(goldWin && playersTurn == "G"){
        println("Gold Win")
        return 100000
    }
    if(goldWin && playersTurn == "S"){
        println("Gold Win")
        return -100000
    }

//    if(goldWin && playersTurn == "S") return -10000
//    if(silverWin && playersTurn == "G") return -10000
    val pieces = evaluatePieces(board, playersTurn)
    val (silverPos, goldPos) = evaluatePositionBoard(board, playersTurn)
    val posGFS = evaluatePositionToGFS(board, playersTurn)
//    board.print()
    if (playersTurn=="G") return 21 * pieces + 10 * goldPos + 1 * posGFS + rand(-10, 10)
    if (playersTurn=="S") return (21 * pieces + 10 * silverPos + 1 * posGFS + rand(-10, 10))
    return 0
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
        value += 10000
    } else if (goldWin) {
        value -= -10000
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

fun evaluatePositionBoard(board: Board, playersTurn: String): Pair<Int,Int> {
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
                        goldEval += -20
                    }
                    if (board.board[row + 1][col].color == "G") {
                        goldEval += -20
                    }
                    if (board.board[row][col - 1].color == "G") {
                        goldEval += -20
                    }
                    if (board.board[row][col + 1].color == "G") {
                        goldEval += -20
                    }
//                    if (board.board[row - 1][col - 1].color == "G") {
//                        goldEval += 50
//                    }
//                    if (board.board[row + 1][col + 1].color == "G") {
//                        goldEval += 50
//                    }
//                    if (board.board[row - 1][col + 1].color == "G") {
//                        goldEval += 50
//                    }
//                    if (board.board[row + 1][col - 1].color == "G") {
//                        goldEval += 50
//                    }
                }
            }
        }
    }
    if (playersTurn == "S") return silverEval - goldEval
    if (playersTurn == "G") return goldEval - silverEval
    return 0
}

fun calcRemainingMoves(boardInput: Board, from: MutableList<Int>, to: MutableList<Int>, remainingMoves: Int): Int {
    val fromRow = from[0]
    val fromCol = from[1]
    val toRow = to[0]
    val toCol = to[1]

    if (boardInput.board[fromRow][fromCol].name == "FS") {
        return 0
    }
    if (((fromRow == toRow - 1) && (fromCol == toCol - 1)) ||
            ((fromRow == toRow - 1) && (fromCol == toCol + 1)) ||
            ((fromRow == toRow + 1) && (fromCol == toCol - 1)) ||
            ((fromRow == toRow + 1) && (fromCol == toCol + 1))) {
        return 0
    } else {
        return remainingMoves - 1
    }

}

fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}

//fun main() {
////    for (i in 1..10) {
////        println(rand(-10,10))
////    }
//
//
//    val board = Board();
//    board.initialize()
//    board.print();
//    board.moveFrontEnd("E8", "E9", "G", 2)
//    board.moveFrontEnd("G8", "G9", "G", 1)
//
////    var abResult = alphaBeta2(board, 3, -10000, 10000, "G", 2)
////    println(abResult)
////    var from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
////    var to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
////    board.moveFrontEnd(from, to, "G", 2)
////    abResult = alphaBeta2(board, 3, -10000, 10000, "G", 1)
////    println(abResult)
////    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
////    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
////    board.moveFrontEnd(from, to, "G", 1)
////    board.print()
//    var abResult = alphaBeta2(board, 3, -10000, 10000, "S", 2)
//    println(abResult)
//    var from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    var to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 2)
//    board.print()
//    board.moveFrontEnd("F8", "E9", "G", 2)
//    board.print()
//    abResult = alphaBeta2(board, 3, -10000, 10000, "S", 2)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 2)
//    board.print()
//    board.moveFrontEnd("F6", "F9", "G", 2)
//    board.print()
//    abResult = alphaBeta2(board, 3, -10000, 10000, "S", 2)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "G", 2)
//    board.print()
//    abResult = alphaBeta2(board, 3, -10000, 10000, "G", 1)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 2)
//    abResult = alphaBeta(board, 3, -10000, 10000, "S", 2)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 1)
////    board.print()
//    abResult = alphaBeta(board, 2, -10000, 10000, "S", 1)
//    println(abResult)
//    board.print()
//    abResult = alphaBeta2(board, 3, -10000, 10000, "G", 2)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "G", 2)
//    board.print()
//    abResult = alphaBeta2(board, 3, -10000, 10000, "G", 1)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 2)
//    abResult = alphaBeta(board, 3, -10000, 10000, "S", 2)
//    println(abResult)
//    from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//    to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//    board.moveFrontEnd(from, to, "S", 1)
////    board.print()
//    abResult = alphaBeta(board, 2, -10000, 10000, "S", 1)
//    println(abResult)
//
//
//
//}

