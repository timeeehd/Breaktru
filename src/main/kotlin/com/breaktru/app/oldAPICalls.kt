package com.breaktru.app

import com.breaktru.code.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

//OLD api calls, just for a reference

//@PostMapping("alphaBeta", produces = [MediaType.APPLICATION_JSON_VALUE])
//fun alphaBetaCall(@RequestBody payload: MoveGen): Map<String, Any> {
//    println("Normal alphabeta")
//    var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
//    var bestBoard = Board()
//    var start = System.currentTimeMillis()
//    var depth = 1
//    var timeSpent = 0L
//    var iterativeDeepeningTime = if (remainingTime < 30000) 9000 else 7000
////        var iterativeDeepeningTime = 8500
//    while (timeSpent + iterativeDeepeningTime < 10000) {
//        var returnedValue = alphaBetaEvalMoveOrdering(board, depth, -10000, 10000, payload.player, payload.remainingMoves)
////            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
//        abResult = returnedValue.first
////            bestBoard = returnedValue.second.first
//        bestBoard = returnedValue.second
//        bestBoard.print()
////            board.transpositionTable = returnedValue.second.second
//        timeSpent = System.currentTimeMillis() - start
//        println("Depth: $depth")
//        println("Timespent: $timeSpent")
//        if (abResult["score"]!![0] > 10000) break
//        depth++
//    }
//    remainingTime -= timeSpent
////        } else if(payload.player == "S") {
////            abResult = alphaBeta2(board, 3, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
////        }
////        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
//    println(abResult)
//
////        val random = (Math.random() * generatedMoves.size).toInt()
////        val entry = generatedMoves.entries.elementAt(random)
////        val entryKey = entry.key
//
//    val from = numberToLetter(abResult["from"]!![1]).toString() + (10 - abResult["from"]!![0] + 1).toString()
////        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
//    val to = numberToLetter(abResult["to"]!![1]).toString() + (10 - abResult["to"]!![0] + 1).toString()
////        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
//    return mapOf("From" to from, "To" to to, "time" to timeSpent)
//}
//
//@PostMapping("alphaBeta2", produces = [MediaType.APPLICATION_JSON_VALUE])
//fun alphaBetaCall2(@RequestBody payload: MoveGen): Map<String, Any> {
//    println("Transposition Table")
////        var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
//    var abResult = mutableListOf(1)
//    var retrievedFrom = mutableListOf(1)
//    var retrievedTo = mutableListOf(1)
//    var bestBoard = Board()
////        if(payload.player == "G") {
//    var start = System.currentTimeMillis()
//    var depth = 1
//    var timeSpent = 0L
//    var iterativeDeepeningTime = if (remainingTime < 300000) 9000 else 8000
//    println(iterativeDeepeningTime)
//    notEnoughtTime = remainingTime < 60000
//    if (notEnoughtTime) {
//        while (depth < 4) {
//            var returnedValue = alphaBetaEvalMoveOrderingTTNull(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
////            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
//            abResult = returnedValue["score"] as MutableList<Int>
//
////            bestBoard = returnedValue.second.first
//            bestBoard = returnedValue["board"] as Board
//            bestBoard.print()
//            board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
//            retrievedFrom = returnedValue["from"] as MutableList<Int>
//            retrievedTo = returnedValue["to"] as MutableList<Int>
////            if(abResult[0] > 10000) break
//            timeSpent = System.currentTimeMillis() - start
//            println("Depth: $depth")
//            println("Timespent: $timeSpent")
//            depth++
//
//        }
//    } else {
////            while (timeSpent + iterativeDeepeningTime < 10000) {
//        while (depth < 4) {
//            var returnedValue = alphaBetaEvalMoveOrderingTTNull(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
////            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
//            abResult = returnedValue["score"] as MutableList<Int>
//
////            bestBoard = returnedValue.second.first
//            bestBoard = returnedValue["board"] as Board
//            bestBoard.print()
//            board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
//            retrievedFrom = returnedValue["from"] as MutableList<Int>
//            retrievedTo = returnedValue["to"] as MutableList<Int>
//
//            timeSpent = System.currentTimeMillis() - start
//            println("Depth: $depth")
//            println("Timespent: $timeSpent")
//            depth++
//            if (abResult[0] > 10000) break
//        }
//    }
//    remainingTime -= timeSpent
////        } else if(payload.player == "S") {
////            abResult = alphaBeta2(board, 3, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
////        }
////        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
//    println(abResult)
//
////        val random = (Math.random() * generatedMoves.size).toInt()
////        val entry = generatedMoves.entries.elementAt(random)
////        val entryKey = entry.key
//
//    val from = numberToLetter(retrievedFrom[1]).toString() + (10 - retrievedFrom[0] + 1).toString()
////        val random2 = (Math.random() * (generatedMoves[entryKey]!!.size)).toInt()
//    val to = numberToLetter(retrievedTo[1]).toString() + (10 - retrievedTo!![0] + 1).toString()
////        val returnString = numberToLetter(generatedMoves[random][0]).toString() + generatedMoves[random][1].toString()
//    return mapOf("From" to from, "To" to to, "time" to timeSpent)
//}
//
//@PostMapping("alphaBeta3", produces = [MediaType.APPLICATION_JSON_VALUE])
//fun alphaBetaCall3(@RequestBody payload: MoveGen): Map<String, Any> {
//    println("Transposition Table")
//    println("remaining turns: ${payload.remainingMoves}")
////        var abResult = mutableMapOf("hi" to mutableListOf(1, 1))
//    var abResult = mutableListOf(1)
//    var retrievedFrom = mutableListOf(1)
//    var retrievedTo = mutableListOf(1)
//    var bestBoard = Board()
////        if(payload.player == "G") {
//    var start = System.currentTimeMillis()
//    var depth = 1
//    var timeSpent = 0L
//    var iterativeDeepeningTime = if (remainingTime < 300000) 9000 else 8000
//    println(iterativeDeepeningTime)
//    notEnoughtTime = remainingTime < 60000
//    if (notEnoughtTime) {
//        while (depth < 4) {
//            var returnedValue = alphaBetaEval3MoveOrdering3TTNull(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
////            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
//            abResult = returnedValue["score"] as MutableList<Int>
//
////            bestBoard = returnedValue.second.first
//            bestBoard = returnedValue["board"] as Board
//            bestBoard.print()
//            board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
//            retrievedFrom = returnedValue["from"] as MutableList<Int>
//            retrievedTo = returnedValue["to"] as MutableList<Int>
////            if(abResult[0] > 10000) break
//            timeSpent = System.currentTimeMillis() - start
//            println("Depth: $depth")
//            println("Timespent: $timeSpent")
//            depth++
//            if (abResult[0] > 10000) break
//        }
//    } else {
////            while (timeSpent + iterativeDeepeningTime < 10000)  {
//
//        while (depth < 4) {
//            var returnedValue = alphaBetaEval3MoveOrdering3TTNull(board, board.transpositionTable, depth, -10000, 10000, payload.player, payload.remainingMoves, true)
////            var returnedValue = alphaBeta4(board, board.transpositionTable,depth, -10000, 10000, payload.player, payload.remainingMoves)
////                var scoreCheck = returnedValue["score"] as MutableList<Int>
////                if (scoreCheck[0)
//            timeSpent = System.currentTimeMillis() - start
//            val finished = returnedValue["finished"]
//            println("finished $finished")
//            if (finished == false) {
//                break
//            }
//            abResult = returnedValue["score"] as MutableList<Int>
//
////            bestBoard = returnedValue.second.first
//            bestBoard = returnedValue["board"] as Board
//            bestBoard.print()
//            board.transpositionTable = returnedValue["tt"] as MutableMap<Long, Map<String, Any>>
//            retrievedFrom = returnedValue["from"] as MutableList<Int>
//            retrievedTo = returnedValue["to"] as MutableList<Int>
//            println("Depth: $depth")
//            println("Timespent: $timeSpent")
//            println("retrievedFrom $retrievedFrom")
//            println("retrievedTo $retrievedTo")
//            if (abResult[0] > 10000) break
//
//            depth++
//        }
//    }
//    remainingTime -= timeSpent
////        } else if(payload.player == "S") {
////            abResult = alphaBeta2(board, 3, Int.MIN_VALUE + 10, Int.MAX_VALUE - 10, payload.player, payload.remainingMoves)
////        }
////        val generatedMoves = moveGenerator(board, payload.player, payload.remainingMoves)
//    println(abResult)
//    println(timeSpent)
//
//
//    val from = numberToLetter(retrievedFrom[1]).toString() + (10 - retrievedFrom[0] + 1).toString()
//    val to = numberToLetter(retrievedTo[1]).toString() + (10 - retrievedTo!![0] + 1).toString()
//    return mapOf("From" to from, "To" to to, "time" to timeSpent)
//}
