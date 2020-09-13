package com.breaktru.code

class Board {

    var board = Array(11) { Array<Ship>(11) { Ship() } }

    fun initialize() {
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

    fun boardString() : Array<String> {
        val strBoard = Array(12) {""}
        for (row in 0..10) {
            when (row) {
                0 -> strBoard[row] += "K| "
                1 -> strBoard[row] += "J| "
                2 -> strBoard[row] += "I| "
                3 -> strBoard[row] += "H| "
                4 -> strBoard[row] += "G| "
                5 -> strBoard[row] += "F| "
                6 -> strBoard[row] += "E| "
                7 -> strBoard[row] += "D| "
                8 -> strBoard[row] += "C| "
                9 -> strBoard[row] += "B| "
                10 -> strBoard[row] += "A| "
                else -> print("Too many rows")
            }
            for (column in 0..20) {
                if (column % 2 == 0) {
                    strBoard[row] += "${board[row][column / 2].color}${board[row][column/2].name}"
                } else {
                    strBoard[row] += " | "
                }
            }
            strBoard[row] += " | "

        }
        strBoard[11] += "    1     2     3     4     5     6     7     8     9    10    11"
        return strBoard
    }


    fun print() {
        for (row in 0..10) {
            when (row) {
                0 -> print("K| ")
                1 -> print("J| ")
                2 -> print("I| ")
                3 -> print("H| ")
                4 -> print("G| ")
                5 -> print("F| ")
                6 -> print("E| ")
                7 -> print("D| ")
                8 -> print("C| ")
                9 -> print("B| ")
                10 -> print("A| ")
                else -> print("Too many rows")
            }
            for (column in 0..20) {
                if (column % 2 == 0) {
                    print("${board[row][column / 2].color}${board[row][column/2].name}")
                } else {
                    print(" | ")
                }
            }
            print(" | ")
            println()
            println()
        }
        println("    1     2     3     4     5     6     7     8     9    10    11")

    }

    fun move(from: String, to: String) {
        val letterFrom = from.first().toUpperCase()
        val letterTo = to.first().toUpperCase()
        val colFrom = from.drop(1).toInt() - 1
        val colTo = from.drop(1).toInt() - 1
        val rowFrom = letterToNumber(letterFrom)
        val shipFrom = board[rowFrom][colFrom]
        board[rowFrom][colFrom] = Ship()
        val rowTo = letterToNumber(letterTo)
        board[rowTo][colTo] = shipFrom
    }

    fun letterToNumber(letter: Char) : Int {
        return when(letter) {
            'A' -> 10
            'B' -> 9
            'C' -> 8
            'D' -> 7
            'E' -> 6
            'F' -> 5
            'G' -> 4
            'H' -> 3
            'I' -> 2
            'J' -> 1
            'K' -> 0
            else -> 100
        }
    }
}

//fun main() {
//    val board = Board();
//    board.initialize()
//    board.print();
//}