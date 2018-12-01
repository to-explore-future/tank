package com.zq.tank.model

import com.zq.tank.interF.Config
import com.zq.tank.interF.View
import org.itheima.kotlin.game.core.Painter


class Grass(override var positionX: Int, override var positionY: Int) : View {


    override val width: Int = Config.block
    override val height: Int = Config.block

    override fun draw() {
        Painter.drawImage("/image/grass.png", positionX, positionY)
    }
}