package com.zq.tank.interF

interface View {

    var positionX: Int
    var positionY: Int

    val width: Int
        get() = Config.block

    val height: Int
        get() = Config.block

    fun draw()

}
