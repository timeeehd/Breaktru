package com.breaktru.app

import com.breaktru.code.Board
import com.breaktru.code.*
import com.breaktru.code.moveGenerator
import com.breaktru.code.numberToLetter
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

var remainingTime = 0L

@RestController
@RequestMapping("/api/")
class MainController {

    var board = Board()

    var prevMove = Board()
    var twoMovesAgo = Board()

    @GetMapping("init", produces = [APPLICATION_JSON_VALUE])
    fun init(): Array<String> {
        board.initialize()
        prevMove.initialize()
        twoMovesAgo.initialize()
        remainingTime = 600000;
        return board.boardString()
    }

    @GetMapping("board", produces = [APPLICATION_JSON_VALUE])
    fun board(): Array<String> {
//        val boardString = board.boardString()
//        println(boardString)
        return board.boardString()
    }

    @PostMapping("move", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun move(@RequestBody payload: Move): Map<String, Array<String>> {
//        println("from: ${payload.from}")
//        println(payload.to)

        twoMovesAgo = boardCopy(prevMove)
        prevMove = boardCopy(board)
        println("prevmove board")
        prevMove.print()
        println("two boards ago move")
        twoMovesAgo.print()

        val result = arrayOf(board.moveFrontEnd(payload.from, payload.to, payload.player, payload.remainingMoves))
        return mapOf("board" to board.boardString(), "result" to result)
    }

    @PostMapping("undo", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun undo(): Map<String, Array<String>> {
//        println("from: ${payload.from}")
//        println(payload.to)
        board = boardCopy(prevMove)
        prevMove = boardCopy(twoMovesAgo)
        twoMovesAgo = Board()
        println("prevmove board")
        prevMove.print()
        println("two boards ago move")
        twoMovesAgo.print()
        return mapOf("board" to board.boardString())
    }

    @PostMapping("generatedMove", produces = [APPLICATION_JSON_VALUE])
    fun genMove(@RequestBody payload: MoveGen): Map<String, String> {
        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
//        println(generatedMoves.size)

        val random = (Math.random() * generatedMoves.size).toInt()
        val entry = generatedMoves.entries.elementAt(random)
//        println(entry)
        val entryKey = entry.key
//        println(entryKey)

        val from = numberToLetter(entryKey[0][1]).toString() + (10 - entryKey[0][0] + 1).toString()
        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
        val to = numberToLetter(entry.value[random2][1]).toString() + (10 - entry.value[random2][0] + 1).toString()
//        println(entry.value[random2][1])
//        println("to: $to   from: $from" )
//        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
        return mapOf("From" to from, "To" to to)
    }

    @PostMapping("alphaBeta", produces = [APPLICATION_JSON_VALUE])
    fun alphaBetaCall(@RequestBody payload: MoveGen): Map<String, Any> {
        println("Normal alphabeta")
        var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
//        var abResult = mutableListOf(1)
        var bestBoard = Board()
//        println(board.escortTable)
//        if(payload.player == "G") {
        var start = System.currentTimeMillis()
        var depth = 1
        var timeSpent = 0L
//        var iterativeDeepeningTime = if(remainingTime < 30000) 9000 else 8000
        var iterativeDeepeningTime = 8500
//        println(iterativeDeepeningTime)
        while (timeSpent + iterativeDeepeningTime < 10000) {
            var returnedValue = alphaBeta3(board, depth, -10000, 10000, payload.player, payload.remainingMoves)
//            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
            abResult = returnedValue.first
//            bestBoard = returnedValue.second.first
            bestBoard = returnedValue.second
            bestBoard.print()
//            board.transpositionTable = returnedValue.second.second
            timeSpent = System.currentTimeMillis() - start
            println("Depth: $depth")
            println("Timespent: $timeSpent")
            if (abResult["score"]!![0] > 10000) break
            depth++
        }
        remainingTime -= timeSpent
//        println(depth)
//        } else if(payload.player == "S") {
//            abResult = alphaBeta2(board, 3, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
//        }
//        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
        println(abResult)
//        println(abResult["from"]!![0])
//        println(generatedMoves.size)

//        val random = (Math.random() * generatedMoves.size).toInt()
//        val entry = generatedMoves.entries.elementAt(random)
////        println(entry)
//        val entryKey = entry.key
////        println(entryKey)

        val from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
//        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
        val to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
//        println(entry.value[random2][1])
//        println("to: $to   from: $from" )
//        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
        return mapOf("From" to from, "To" to to, "time" to timeSpent)
    }

    @PostMapping("alphaBeta2", produces = [APPLICATION_JSON_VALUE])
    fun alphaBetaCall2(@RequestBody payload: MoveGen): Map<String, Any> {
        println("Transposition Table")
//        var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
        var abResult = mutableListOf(1)
        var retrievedFrom = mutableListOf(1)
        var retrievedTo = mutableListOf(1)
        var bestBoard = Board()
//        println(board.escortTable)
//        if(payload.player == "G") {
        var start = System.currentTimeMillis()
        var depth = 1
        var timeSpent = 0L
        while (timeSpent + 9000 < 10000) {
//        while (depth < 4) {
            var returnedValue = alphaBeta7(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
//            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
            abResult = returnedValue["score"] as MutableList<Int>

//            bestBoard = returnedValue.second.first
            bestBoard = returnedValue["board"] as Board
            bestBoard.print()
            board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
            retrievedFrom = returnedValue["from"] as MutableList<Int>
            retrievedTo = returnedValue["to"] as MutableList<Int>
//            if(abResult[0] > 10000) break
            timeSpent = System.currentTimeMillis() - start
            println("Depth: $depth")
            println("Timespent: $timeSpent")
            depth++
        }

//        println(depth)
//        } else if(payload.player == "S") {
//            abResult = alphaBeta2(board, 3, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
//        }
//        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
        println(abResult)
//        println(abResult["from"]!![0])
//        println(generatedMoves.size)

//        val random = (Math.random() * generatedMoves.size).toInt()
//        val entry = generatedMoves.entries.elementAt(random)
////        println(entry)
//        val entryKey = entry.key
////        println(entryKey)

        val from = numberToLetter(retrievedFrom[1]).toString() + (10 - retrievedFrom[0] + 1).toString()
//        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
        val to = numberToLetter(retrievedTo[1]).toString() + (10 - retrievedTo!![0] + 1).toString()
//        println(entry.value[random2][1])
//        println("to: $to   from: $from" )
//        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
        return mapOf("From" to from, "To" to to, "time" to timeSpent)
    }
}