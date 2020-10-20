package com.breaktru.code

import java.util.*

open class Ship(open val name: String = "  ", open val color: String = " ", open val type: String = " ", open val id: UUID = UUID.randomUUID()) {
}

class FlagShip(override val color: String) : Ship() {
    override val name = "FS"
    override val type = "FlagShip"
}

class Escort(override val color: String) : Ship() {
    override val name = "E"
    override val type = "Escort"
}
