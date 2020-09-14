package com.breaktru.code

class Board {

    var board = Array(11) { Array<Ship>(11) { Ship() } }

    fun initialize() {
        // To initialize board as empty, so if you want to restart
        // everything is at the start position again.
        for(row in 0..10){
            for(column in 0..10){
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

    fun boardString() : Array<String> {
        val strBoard = Array(12) {""}
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
                    strBoard[row] += "${board[row][column / 2].color}${board[row][column/2].name}"
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

    fun move(from: String, to: String, playerMove: String) : String {
        val letterFrom = from.first().toUpperCase()
        val letterTo = to.first().toUpperCase()
        val rowFrom = from.drop(1).toInt() - 1
        val rowTo = to.drop(1).toInt() - 1
        val colFrom = letterToNumber(letterFrom)
        val colTo = letterToNumber(letterTo)
        val shipFrom = board[rowFrom][colFrom]
        var stringReturn = ""
        if(shipFrom.color != playerMove){
            return "YOU ARE NOT ALLOWED TO MOVE THIS PIECE"
        }
        val shipFromType = shipFrom.type
        if(shipFromType == "FlagShip") {
            if(letterTo == 'A' || letterTo == 'K' || colTo == 0 || colTo == 10)
            {
//                board[rowTo][colTo] = shipFrom
                stringReturn = "GAME WON by $playerMove"
            }
        }

        board[rowFrom][colFrom] = Ship()
        val shipTo = if(board[rowTo][colTo].name == " ") Ship() else board[rowTo][colTo]
        println(shipTo.type)
        if (shipTo.type == "FlagShip") {
            stringReturn = "GAME WON by $playerMove"
        }
        board[rowTo][colTo] = shipFrom
        stringReturn = if(stringReturn.isEmpty()) "No winner" else stringReturn
        return stringReturn
    }

    private fun letterToNumber(letter: Char) : Int {
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