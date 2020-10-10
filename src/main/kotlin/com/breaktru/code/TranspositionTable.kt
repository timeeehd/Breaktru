package com.breaktru.code

import kotlin.math.pow
import kotlin.random.Random

//val transpositionTable = mutableMapOf<Long, Map<String, Any>>()
var count = 0

fun buildZobristTable(): Pair<Array<Array<Array<Long>>>, Array<Array<Long>>> {
    val escortZobrist = Array(2) { Array(11) { Array(11) { 0L } } }
    val fSZobrist = Array(11) { Array(11) { 0L } }
    //0: Silver
    //1: Gold
    for (i in 0..1)
        for (k in 0..10)
            for (j in 0..10)
                escortZobrist[i][k][j] = Random.nextLong(2F.pow(64).toLong())

    for (i in 0..10)
        for (j in 0..10)
            fSZobrist[i][j] = Random.nextLong(2F.pow(64).toLong())

    return Pair(escortZobrist, fSZobrist)
}

fun getZobristHash(board: Board): Long {
    var zobristHash = 0L
    for (i in 0..10) {
        for (j in 0..10) {
            if (board.board[i][j].name != "  ") {
                if (board.board[i][j].color == "S") {
                    zobristHash = zobristHash xor board.escortTable[0][i][j]
                } else if (board.board[i][j].color == "G" && board.board[i][j].name != "FS") {
                    zobristHash = zobristHash xor board.escortTable[1][i][j]
                } else if (board.board[i][j].name == "FS") {
                    zobristHash = zobristHash xor board.fsTable[i][j]
                }
            }
        }
    }
    return zobristHash
}

fun retrieve(board: Board, table: MutableMap<Long, Map<String, Any>>) : Map<String, Any> {
    val zobristHash = getZobristHash(board)
    val test = table[zobristHash ushr 44] ?: return mapOf("depth" to -1)
//    println(test)
    return test
}

fun store(board: Board, table: MutableMap<Long, Map<String, Any>>,from: MutableList<Int>, to: MutableList<Int>, score: Int,flag: String, depth: Int, playersTurn: String ): MutableMap<Long, Map<String, Any>> {
//    board.print()
    val zobristHash = getZobristHash(board)
    table[zobristHash ushr 44] = mapOf("key" to zobristHash,
            "from" to from, "to" to to, "score" to score,
            "flag" to flag, "depth" to depth, "playersTurn" to playersTurn)
//    count++
//    println(count)
    return table
}

//fun main() {
//    val (escortTable, fSTable) = buildZobristTable()
//
//    val board = Board()
//    board.initialize()
//    val zobristHash = getZobristHash(board)
//    val test = retrieve(board)
//    println(test)
//}