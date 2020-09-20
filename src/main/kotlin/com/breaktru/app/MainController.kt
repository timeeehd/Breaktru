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
    fun init(): Array<String>{
        board.initialize()
        return board.boardString()
    }

    @GetMapping("board", produces = [APPLICATION_JSON_VALUE])
    fun board(): Array<String>{
        val boardString = board.boardString()
        println(boardString)
        return board.boardString()
    }

    @PostMapping("move", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun move(@RequestBody payload: Move) : Map<String,Array<String>>{
        println(payload.player)
        val result = arrayOf(board.move(payload.from, payload.to, payload.player))
        return mapOf("board" to board.boardString(), "result" to result)
    }

    @GetMapping("generatedMove", produces = [APPLICATION_JSON_VALUE])
    fun genMove(): Map<String, String> {
        val generatedMoves = board.moveGenerator("S")
        println(generatedMoves.size)

        val random = (Math.random() * generatedMoves.size).toInt()
        val entry = generatedMoves.entries.elementAt(random)
        val entryKey = entry.key
        println(entryKey)
        val from = numberToLetter(entryKey[0][0]).toString() + (entryKey[0][1] + 1).toString()
        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
        val to = numberToLetter(entry.value[random2][0]).toString() + (entry.value[random2][1] + 1).toString()
//        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
        return mapOf("From" to from, "To" to to)
    }
}