package com.breaktru.code

open class Ship(open val name: String = "  ", open val color: String = " ") {
    //val captured = false
}

class FlagShip(override val color: String): Ship() {
    override val name = "FS"
}

class Escort(override val color: String): Ship(){
    override val name = "E "
}


//fun main() {
//    val ship = FlagShip("W")
//    println(ship.name)
//    println(ship.captured)
//}