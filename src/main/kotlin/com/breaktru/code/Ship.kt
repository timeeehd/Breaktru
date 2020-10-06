package com.breaktru.code

import java.util.*

open class Ship(open val name: String = "  ", open val color: String = " ", open val type: String = " ", open val id: UUID = UUID.randomUUID()) {
    //val captured = false
}

class FlagShip(override val color: String) : Ship() {
    override val name = "FS"
    override val type = "FlagShip"
}

class Escort(override val color: String) : Ship() {
    override val name = "E"
    override val type = "Escort"
}


//fun main() {
//    val ship = FlagShip("W")
//    println(ship.name)
//    println(ship.captured)
//}