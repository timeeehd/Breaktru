package com.breaktru.code

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
    if (color == "G" && remainingMoves == 1) println("$color + $remainingMoves gives us $testMap")
//    println(testMap)
    return testMap
}

fun moveGenerator2(board: Board, color: String, remainingMoves: Int): Map<String, MutableMap<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>> {
//        println("remaining moves ${remainingMoves}")
    var possibleMovesOnePosition: MutableList<MutableList<Int>> = ArrayList()
    var captureMoveOnePosition: MutableList<MutableList<Int>> = ArrayList()
    val flagShipMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()

    val possibleMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    val captureMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    for (row in 0..10) {
        col@ for (col in 0..10) {
            possibleMovesOnePosition = ArrayList()
            captureMoveOnePosition = ArrayList()
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
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        } else if (row == 0 && col == 10) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                        } else if (row == 0) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }

                        } else if (row == 10 && col == 0) {

                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (row == 10 && col == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                        } else if (row == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 0) {

                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 10) {

                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }

                        } else {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        }
                    }

                    for (up in row - 1 downTo 0) {
                        if (board.board[up][col].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(up, col))
                        }
                    }
                    for (down in (row + 1)..10) {
                        if (board.board[down][col].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(down, col))
                        }
                    }
                    for (left in col - 1 downTo 0) {
                        if (board.board[row][left].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(row, left))
                        }
                    }
                    for (right in (col + 1)..10) {
                        if (board.board[row][right].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(row, right))
                        }
                    }
                }
            }

            currentPostion.add(mutableListOf(row, col))
            if (possibleMovesOnePosition.isNotEmpty()) {
                if (board.board[row][col].name == "FS") {
                    flagShipMoves[currentPostion] = possibleMovesOnePosition
                } else {
                    possibleMoves[currentPostion] = possibleMovesOnePosition
                }
            }
            if (captureMoveOnePosition.isNotEmpty()) {
                captureMoves[currentPostion] = captureMoveOnePosition
            }

        }

    }


    val allMoves = mapOf("capture" to captureMoves, "flagship" to flagShipMoves, "moves" to possibleMoves)


    if (color == "G" && remainingMoves == 1 && flagShipMoves.isNotEmpty()) println("DIT MAG NIET!!!")
//    println(testMap)
    return allMoves
}

fun moveGenerator3(board: Board, color: String, remainingMoves: Int, transMoveFrom: MutableList<Int>, transMoveTo: MutableList<Int>): Map<String, MutableMap<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>> {
//        println("remaining moves ${remainingMoves}")
    var possibleMovesOnePosition: MutableList<MutableList<Int>> = ArrayList()
    var captureMoveOnePosition: MutableList<MutableList<Int>> = ArrayList()
    val flagShipMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()

    val possibleMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    val captureMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    val transMove = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    var transMoveFromList: MutableList<MutableList<Int>> = ArrayList()
    val transMoveToList :MutableList<MutableList<Int>> = ArrayList()

    //TODO: CHECK THE LENGTH OF TRANSMOVEFROM AND TO FOR LENGHT TO SEE IF POSSIBLE TO AVOID CRASH
    if( transMoveFrom.size != 0 || transMoveTo.size != 0) {
//        println("Transposition move is activated beep boop")
        transMoveFromList.add(mutableListOf(transMoveFrom[0], transMoveFrom[1]))
        transMoveToList.add(mutableListOf(transMoveTo[0], transMoveTo[1]))
    }

    transMove[transMoveFromList] = transMoveToList
    for (row in 0..10) {
        col@ for (col in 0..10) {
            possibleMovesOnePosition = ArrayList()
            captureMoveOnePosition = ArrayList()
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
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        } else if (row == 0 && col == 10) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                        } else if (row == 0) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }

                        } else if (row == 10 && col == 0) {

                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (row == 10 && col == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                        } else if (row == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 0) {

                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }

                        } else if (col == 10) {

                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }

                        } else {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        }
                    }

                    for (up in row - 1 downTo 0) {
                        if (board.board[up][col].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(up, col))
                        }
                    }
                    for (down in (row + 1)..10) {
                        if (board.board[down][col].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(down, col))
                        }
                    }
                    for (left in col - 1 downTo 0) {
                        if (board.board[row][left].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(row, left))
                        }
                    }
                    for (right in (col + 1)..10) {
                        if (board.board[row][right].color != " ") {
                            break
                        } else {
                            possibleMovesOnePosition.add(mutableListOf(row, right))
                        }
                    }
                }
            }

            currentPostion.add(mutableListOf(row, col))
            if (possibleMovesOnePosition.isNotEmpty()) {
                if (board.board[row][col].name == "FS") {
                    flagShipMoves[currentPostion] = possibleMovesOnePosition
                } else {
                    possibleMoves[currentPostion] = possibleMovesOnePosition
                }
            }
            if (captureMoveOnePosition.isNotEmpty()) {
                captureMoves[currentPostion] = captureMoveOnePosition
            }

        }

    }


    val allMoves = mapOf("transposition" to transMove,"capture" to captureMoves, "flagship" to flagShipMoves, "moves" to possibleMoves)


    if (color == "G" && remainingMoves == 1 && flagShipMoves.isNotEmpty()) println("DIT MAG NIET!!!")
//    println(testMap)
    return allMoves
}