package com.breaktru.code

import kotlin.math.pow
import kotlin.random.Random

fun buildZobristTable(): Pair<Array<Array<Array<Long>>>, Array<Array<Long>>> {
    val escortZobrist = Array(2) { Array(11) { Array(11) {0L} } }
    val fSZobrist = Array(11) { Array(11) {0L} }
    //0: Silver
    //1: Gold
    for (i in 0..1)
        for (k in 0..10)
            for(j in 0..10)
                escortZobrist[i][k][j] = Random.nextLong(2F.pow(64).toLong())

    for (i in 0..10)
        for(j in 0..10)
            fSZobrist[i][j] = Random.nextLong(2F.pow(64).toLong())

    return Pair(escortZobrist,fSZobrist)
}

fun getZobristHash(board: Board, escortTable: Array<Array<Array<Long>>>, fsTable: Array<Array<Long>>) : Long {
    var zobristHash = 0L
    for(i in 0..10) {
        for (j in 0..10) {
            if (board.board[i][j].name != "  ") {
                if (board.board[i][j].color == "S") {
                    zobristHash = zobristHash xor escortTable[0][i][j]
                } else if (board.board[i][j].color == "G" && board.board[i][j].name != "FS") {
                    zobristHash = zobristHash xor escortTable[1][i][j]
                } else if (board.board[i][j].name == "FS") {
                    zobristHash = zobristHash xor fsTable[i][j]
                }
            }
        }
    }
    return zobristHash
}

fun main() {
    val (escortTable, fSTable) = buildZobristTable()

    val board = Board()
    board.initialize()
    val zobristHash = getZobristHash(board,escortTable,fSTable)
    println(zobristHash)
}