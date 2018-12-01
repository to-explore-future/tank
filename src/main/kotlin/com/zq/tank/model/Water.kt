package com.zq.tank.model

import com.zq.tank.interF.Config
import com.zq.tank.interF.Obstructive
import org.itheima.kotlin.game.core.Painter


class Water(override var positionX: Int, override var positionY: Int) : Obstructive {

    override val width: Int = Config.block
    override val height: Int = Config.block

    override fun draw() {
        Painter.drawImage("/image/water.png", positionX, positionY)
    }
}