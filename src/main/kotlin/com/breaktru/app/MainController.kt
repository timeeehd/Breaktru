package com.breaktru.app

import com.breaktru.code.Board
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

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
        return board.boardString()
    }

    @PostMapping("move", produces = [APPLICATION_JSON_VALUE])
    fun move(@RequestBody payload: Move) : Array<String>{
        board.move(payload.from, payload.to)
        return board.boardString()
    }
}