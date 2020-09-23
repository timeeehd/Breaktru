package com.breaktru.app

import com.breaktru.code.Board
import com.breaktru.code.numberToLetter
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/")
class MainController {

    val board = Board()


    @GetMapping("test", produces = [APPLICATION_JSON_VALUE])
    fun test() = "test"

    @GetMapping("init", produces = [APPLICATION_JSON_VALUE])
    fun init(): Array<String> {
        board.initialize()
        return board.boardString()
    }

    @GetMapping("board", produces = [APPLICATION_JSON_VALUE])
    fun board(): Array<String> {
        val boardString = board.boardString()
//        println(boardString)
        return board.boardString()
    }

    @PostMapping("move", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun move(@RequestBody payload: Move): Map<String, Array<String>> {
//        println("from: ${payload.from}")
//        println(payload.to)
        val result = arrayOf(board.move(payload.from, payload.to, payload.player, payload.remainingMoves))
        return mapOf("board" to board.boardString(), "result" to result)
    }

    @PostMapping("generatedMove", produces = [APPLICATION_JSON_VALUE])
    fun genMove(@RequestBody payload: MoveGen): Map<String, String> {
        val generatedMoves = board.moveGenerator(payload.player, payload.remainingMoves)
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
}