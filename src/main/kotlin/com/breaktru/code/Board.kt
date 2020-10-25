package com.breaktru.code

import java.util.*

class Board {

    var board = Array(11) { Array<Ship>(11) { Ship() } }
    var lastMove: UUID? = null
    lateinit var lastPlayer: String
    lateinit var escortTable: Array<Array<Array<Long>>>
    lateinit var fsTable: Array<Array<Long>>

    var transpositionTable = mutableMapOf<Long, Map<String, Any>>()

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

        // get the zobristHash tables ready for the transposition table
        val zobristTable = buildZobristTable()
        escortTable = zobristTable.first
        fsTable = zobristTable.second
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
                    if(board[row][column / 2].name == "  "){
                        print("        ")
                    } else if((board[row][column / 2].name == "E")) {
                        print("${board[row][column / 2].color}${board[row][column / 2].name}   ")
                    } else {
                        print("${board[row][column / 2].color}${board[row][column / 2].name}")
                    }
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

    fun moveFrontEnd(from: String, to: String, playerMove: String, remainingMoves: Int): String {
        val letterFrom = from.first().toUpperCase()
        val letterTo = to.first().toUpperCase()
        val rowFrom = 10 - (from.drop(1).toInt() - 1)
        val rowTo = 10 - (to.drop(1).toInt() - 1)
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
        var stringReturn = ""
        if (shipFrom.color != playerMove) {
            return "YOU ARE NOT ALLOWED TO MOVE THIS PIECE"
        }
        val shipFromType = shipFrom.type
        if (shipFromType == "FlagShip") {
            if (letterTo == 'A' || letterTo == 'K' || rowTo == 0 || rowTo == 10) {
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

    fun moveBackEnd(from: MutableList<Int>, to: MutableList<Int>, remainingMoves: Int) {
        val fromRow = from[0]
        val fromCol = from[1]
        val toRow = to[0]
        val toCol = to[1]
        if (remainingMoves == 2) lastMove = board[fromRow][fromCol].id
        if (remainingMoves == 1) lastMove = null
        board[toRow][toCol] = board[fromRow][fromCol]
        board[fromRow][fromCol] = Ship()
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

fun boardCopy(board: Board): Board {
    val boardCopy = Board()
    boardCopy.escortTable = board.escortTable
    boardCopy.fsTable = board.fsTable
    for (row in 0..10) {
        for (col in 0..10) {
            boardCopy.board[row][col] = board.board[row][col]
        }
    }
    boardCopy.lastMove = board.lastMove
    return boardCopy
}