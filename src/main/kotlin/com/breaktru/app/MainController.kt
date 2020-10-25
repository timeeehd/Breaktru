package com.breaktru.app

import com.breaktru.code.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

var remainingTime = 0L
var notEnoughtTime = false

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
        return board.boardString()
    }

    @PostMapping("move", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun move(@RequestBody payload: Move): Map<String, Array<String>> {

        twoMovesAgo = boardCopy(prevMove)
        prevMove = boardCopy(board)

        val result = arrayOf(board.moveFrontEnd(payload.from, payload.to, payload.player, payload.remainingMoves))
        return mapOf("board" to board.boardString(), "result" to result)
    }

    @PostMapping("undo", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun undo(): Map<String, Array<String>> {
        board = boardCopy(prevMove)
        prevMove = boardCopy(twoMovesAgo)
        twoMovesAgo = Board()
        return mapOf("board" to board.boardString())
    }

    // random move generator
    @PostMapping("generatedMove", produces = [APPLICATION_JSON_VALUE])
    fun genMove(@RequestBody payload: MoveGen): Map<String, String> {
        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)

        val random = (Math.random() * generatedMoves.size).toInt()
        val entry = generatedMoves.entries.elementAt(random)
        val entryKey = entry.key

        val from = numberToLetter(entryKey[0][1]).toString() + (10 - entryKey[0][0] + 1).toString()
        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
        val to = numberToLetter(entry.value[random2][1]).toString() + (10 - entry.value[random2][0] + 1).toString()
        return mapOf("From" to from, "To" to to)
    }

    // the call the run the alpha beta search function
    @PostMapping("alphaBeta", produces = [APPLICATION_JSON_VALUE])
    fun alphaBetaCall(@RequestBody payload: MoveGen): Map<String, Any> {
        board.transpositionTable = mutableMapOf<Long, Map<String, Any>>()
        var abResult = mutableListOf(1)
        var retrievedFrom = mutableListOf(1)
        var retrievedTo = mutableListOf(1)
        var bestBoard = Board()
        var start = System.currentTimeMillis()
        var depth = 1
        var timeSpent = 0L
        // if you have less then 4 minutes left, search only 3 deep
        notEnoughtTime = remainingTime < 240000
        if (notEnoughtTime) {
            while (depth < 4) {
                var returnedValue = alphaBetaEval3MoveOrdering3TTNull(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
                abResult = returnedValue["score"] as MutableList<Int>

                bestBoard = returnedValue["board"] as Board
                bestBoard.print()
                board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
                retrievedFrom = returnedValue["from"] as MutableList<Int>
                retrievedTo = returnedValue["to"] as MutableList<Int>
                timeSpent = System.currentTimeMillis() - start
                println("Depth: $depth")
                println("Timespent: $timeSpent")
                depth++
                if (abResult[0] > 10000) break
            }
        } else {
            while (timeSpent< 10000)  {
                var returnedValue = alphaBetaEval3MoveOrdering3TTNullID(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true, start)
                timeSpent = System.currentTimeMillis() - start
                val finished = returnedValue["finished"]
                if (finished == false) break
                abResult = returnedValue["score"] as MutableList<Int>

                bestBoard = returnedValue["board"] as Board
                bestBoard.print()
                board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
                retrievedFrom = returnedValue["from"] as MutableList<Int>
                retrievedTo = returnedValue["to"] as MutableList<Int>
                if (abResult[0] > 10000) break
                println("Depth: $depth")
                println("Timespent: $timeSpent")
                depth++
            }
        }
        remainingTime -= timeSpent
        println(abResult)
        println(timeSpent)

        val from = numberToLetter(retrievedFrom[1]).toString() + (10 - retrievedFrom[0] + 1).toString()
        val to = numberToLetter(retrievedTo[1]).toString() + (10 - retrievedTo!![0] + 1).toString()
        return mapOf("From" to from, "To" to to, "time" to timeSpent)
    }
}