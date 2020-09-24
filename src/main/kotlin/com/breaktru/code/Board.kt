package com.breaktru.code

import java.util.*
import kotlin.collections.ArrayList

class Board {

    var board = Array(11) { Array<Ship>(11) { Ship() } }
    var lastMove: UUID? = null
    lateinit var lastPlayer: String

    fun initialize() {
        // To initialize board as empty, so if you want to restart
        // everything is at the start position again.
        for (row in 0..10) {
            for (column in 0..10) {
                board[row][column] = Ship()
            }
        }
        board[1][3] = Escort("S")
        board[1][4] = Escort("S")
        board[1][5] = Escort("S")
        board[1][6] = Escort("S")
        board[1][7] = Escort("S")
        board[3][1] = Escort("S")
        board[3][4] = Escort("G")
        board[3][5] = Escort("G")
        board[3][6] = Escort("G")
        board[3][9] = Escort("S")
        board[4][1] = Escort("S")
        board[4][3] = Escort("G")
        board[4][7] = Escort("G")
        board[4][9] = Escort("S")
        board[5][1] = Escort("S")
        board[5][3] = Escort("G")
        board[5][5] = FlagShip("G")
        board[5][7] = Escort("G")
        board[5][9] = Escort("S")
        board[6][1] = Escort("S")
        board[6][3] = Escort("G")
        board[6][7] = Escort("G")
        board[6][9] = Escort("S")
        board[7][1] = Escort("S")
        board[7][4] = Escort("G")
        board[7][5] = Escort("G")
        board[7][6] = Escort("G")
        board[7][9] = Escort("S")
        board[9][3] = Escort("S")
        board[9][4] = Escort("S")
        board[9][5] = Escort("S")
        board[9][6] = Escort("S")
        board[9][7] = Escort("S")
    }

    fun boardString(): Array<String> {
        val strBoard = Array(12) { "" }
        for (row in 0..10) {
            when (row) {
                0 -> strBoard[row] += "11| "
                1 -> strBoard[row] += "10| "
                2 -> strBoard[row] += "9 | "
                3 -> strBoard[row] += "8 | "
                4 -> strBoard[row] += "7 | "
                5 -> strBoard[row] += "6 | "
                6 -> strBoard[row] += "5 | "
                7 -> strBoard[row] += "4 | "
                8 -> strBoard[row] += "3 | "
                9 -> strBoard[row] += "2 | "
                10 -> strBoard[row] += "1 | "
                else -> print("Too many rows")
            }
            for (column in 0..20) {
                if (column % 2 == 0) {
                    strBoard[row] += "${board[row][column / 2].color}${board[row][column / 2].name}"
                } else {
                    strBoard[row] += " | "
                }
            }
            strBoard[row] += " | "

        }
        strBoard[11] += "     A     B     C     D     E     F     G     H     I     J     K"
        return strBoard
    }


    fun print() {
        for (row in 0..10) {
            when (row) {
                0 -> print("1 | ")
                1 -> print("2 | ")
                2 -> print("3 | ")
                3 -> print("4 | ")
                4 -> print("5 | ")
                5 -> print("6 | ")
                6 -> print("7 | ")
                7 -> print("8 | ")
                8 -> print("9 | ")
                9 -> print("10| ")
                10 -> print("11| ")
                else -> print("Too many rows")
            }
            for (column in 0..20) {
                if (column % 2 == 0) {
                    print("${board[row][column / 2].color}${board[row][column / 2].name}")
                } else {
                    print(" | ")
                }
            }
            print(" | ")
            println()
            println()
        }
        println("     A     B     C     D     E     F     G     H     I     J     K")

    }

    fun move(from: String, to: String, playerMove: String, remainingMoves: Int): String {
        val letterFrom = from.first().toUpperCase()
        val letterTo = to.first().toUpperCase()
        val rowFrom = 10 - (from.drop(1).toInt() - 1)
//        println(rowFrom)
        val rowTo = 10 - (to.drop(1).toInt() - 1)
//        println(rowTo)
        val colFrom = letterToNumber(letterFrom)
        val colTo = letterToNumber(letterTo)
        val shipFrom = board[rowFrom][colFrom]
        if (remainingMoves == 2) lastMove = shipFrom.id
        if (remainingMoves == 1 && playerMove == lastPlayer) lastMove = null
        val shipTo = if (board[rowTo][colTo].name == "  ") Ship() else board[rowTo][colTo]
        if (((rowTo != rowFrom && colTo != colFrom) &&
                        (rowTo != rowFrom - 1 && colTo != colFrom - 1) &&
                        (rowTo != rowFrom + 1 && colTo != colFrom + 1) &&
                        (rowTo != rowFrom - 1 && colTo != colFrom + 1) &&
                        (rowTo != rowFrom + 1 && colTo != colFrom - 1)) ||
                (shipFrom.type == "FlagShip" && remainingMoves != 2)) {
            return "ILLEGAL MOVE"
        }
//        if ((shipFrom.type == "FlagShip" && remainingMoves != 2)) {
//            return "ILLEGAL MOVE"
//        }
        var stringReturn = ""
        if (shipFrom.color != playerMove) {
            return "YOU ARE NOT ALLOWED TO MOVE THIS PIECE"
        }
        val shipFromType = shipFrom.type
        if (shipFromType == "FlagShip") {
            if (letterTo == 'A' || letterTo == 'K' || rowTo == 0 || rowTo == 10) {
//                board[rowTo][colTo] = shipFrom
                stringReturn = "GAME WON by $playerMove"
            }
        }

        board[rowFrom][colFrom] = Ship()
        if (shipTo.type == "FlagShip") {
            stringReturn = "GAME WON by $playerMove"
        }
        board[rowTo][colTo] = shipFrom

        stringReturn = if (stringReturn.isEmpty()) "No winner" else stringReturn
        lastPlayer = playerMove
        return stringReturn
    }

    fun moveGenerator(color: String, remainingMoves: Int): MutableMap<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>> {
//        println("remaining moves ${remainingMoves}")
        var possibleMoves: MutableList<MutableList<Int>> = ArrayList()
        val testMap = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
        for (row in 0..10) {
            col@ for (col in 0..10) {
                possibleMoves = ArrayList()
                var currentPostion: MutableList<MutableList<Int>> = ArrayList()
                if (board[row][col].color == color) {
                    if (board[row][col].id != lastMove) {
                        if (remainingMoves == 1 && board[row][col].name == "FS") {
                        println("Hier mag ik niet komen")
                            continue@col
                        }
                        if (remainingMoves != 1) {
                            if (row == 0 && col == 0) {

                                if (board[row + 1][col + 1].color != color && board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row + 1, col + 1))
                                }
                            } else if (row == 0 && col == 10) {
                                if (board[row + 1][col - 1].color != color && board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                    possibleMoves.add(mutableListOf(row + 1, col - 1))
                                }
                            } else if (row == 0) {
                                if (board[row + 1][col - 1].color != color && board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                    possibleMoves.add(mutableListOf(row + 1, col - 1))
                                }
                                if (board[row + 1][col + 1].color != color && board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row + 1, col + 1))
                                }

                            } else if (row == 10 && col == 0) {

                                if (board[row - 1][col + 1].color != color && board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col + 1))
                                }
                            } else if (row == 10 && col == 10) {
                                if (board[row - 1][col - 1].color != color && board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col - 1))
                                }
                            } else if (row == 10) {
                                if (board[row - 1][col - 1].color != color && board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col - 1))
                                }
                                if (board[row - 1][col + 1].color != color && board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col + 1))
                                }

                            } else if (col == 0) {

                                if (board[row + 1][col + 1].color != color && board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row + 1, col + 1))
                                }
                                if (board[row - 1][col + 1].color != color && board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col + 1))
                                }

                            } else if (col == 10) {

                                if (board[row + 1][col - 1].color != color && board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                    possibleMoves.add(mutableListOf(row + 1, col - 1))
                                }
                                if (board[row - 1][col - 1].color != color && board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col - 1))
                                }

                            } else {
                                if (board[row - 1][col - 1].color != color && board[row - 1][col - 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col - 1))
                                }
                                if (board[row - 1][col + 1].color != color && board[row - 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row - 1, col + 1))
                                }
                                if (board[row + 1][col - 1].color != color && board[row + 1][col - 1].color != " ") {
//                                println("Capture Move")

                                    possibleMoves.add(mutableListOf(row + 1, col - 1))
                                }
                                if (board[row + 1][col + 1].color != color && board[row + 1][col + 1].color != " ") {
//                                println("Capture Move")
                                    possibleMoves.add(mutableListOf(row + 1, col + 1))
                                }
                            }
                        }

                        for (up in row - 1 downTo 0) {
                            if (board[up][col].color != " ") {
                                break
                            } else {
                                possibleMoves.add(mutableListOf(up, col))
                            }
                        }
                        for (down in (row + 1)..10) {
                            if (board[down][col].color != " ") {
                                break
                            } else {
                                possibleMoves.add(mutableListOf(down, col))
                            }
                        }
                        for (left in col - 1 downTo 0) {
                            if (board[row][left].color != " ") {
                                break
                            } else {
                                possibleMoves.add(mutableListOf(row, left))
                            }
                        }
                        for (right in (col + 1)..10) {
                            if (board[row][right].color != " ") {
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
        println(testMap)
        return testMap
    }

    private fun letterToNumber(letter: Char): Int {
        return when (letter) {
            'A' -> 0
            'B' -> 1
            'C' -> 2
            'D' -> 3
            'E' -> 4
            'F' -> 5
            'G' -> 6
            'H' -> 7
            'I' -> 8
            'J' -> 9
            'K' -> 10
            else -> 100
        }
    }
}

//fun main() {
//    val board = Board();
//    board.initialize()
//    board.print();
//    board.moveGenerator("G",1)
////    board.move("E8", "E9", "G")
////    board.move("F8", "F9", "G")
////    board.moveGenerator("S",2)
//}