package com.breaktru.code

// How I generate my moves, taking into account if a player already moved an escort ship or not
// and which ship it moved
fun finalMoveGeneration(board: Board, color: String, remainingMoves: Int, transMoveFrom: MutableList<Int>, transMoveTo: MutableList<Int>): Map<String, MutableMap<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>> {
    var possibleMovesOnePosition: MutableList<MutableList<Int>> = ArrayList()
    var captureMoveOnePosition: MutableList<MutableList<Int>> = ArrayList()
    val flagShipMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()

    val possibleMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    val captureMoves = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    val transMove = mutableMapOf<MutableList<MutableList<Int>>, MutableList<MutableList<Int>>>()
    var transMoveFromList: MutableList<MutableList<Int>> = ArrayList()
    val transMoveToList :MutableList<MutableList<Int>> = ArrayList()

    if( transMoveFrom.size != 0 || transMoveTo.size != 0) {
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
                        continue@col
                    }
                    if (remainingMoves != 1) {
                        if (row == 0 && col == 0) {
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        } else if (row == 0 && col == 10) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                        } else if (row == 0) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                        } else if (row == 10 && col == 0) {
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (row == 10 && col == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                        } else if (row == 10) {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (col == 0) {
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col + 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                        } else if (col == 10) {
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                        } else {
                            if (board.board[row - 1][col - 1].color != color && board.board[row - 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col - 1))
                            }
                            if (board.board[row - 1][col + 1].color != color && board.board[row - 1][col + 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row - 1, col + 1))
                            }
                            if (board.board[row + 1][col - 1].color != color && board.board[row + 1][col - 1].color != " ") {
                                captureMoveOnePosition.add(mutableListOf(row + 1, col - 1))
                            }
                            if (board.board[row + 1][col + 1].color != color && board.board[row + 1][col + 1].color != " ") {
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
    return allMoves
}