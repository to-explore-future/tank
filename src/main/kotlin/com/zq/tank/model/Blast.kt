package com.zq.tank.model

import com.zq.tank.interF.AutoDestroy
import com.zq.tank.interF.Config
import com.zq.tank.interF.View
import org.itheima.kotlin.game.core.Painter

class Blast(override var positionX: Int, override var positionY: Int,
            override val width: Int = Config.block, override val height: Int = Config.block)
    : View, AutoDestroy {

    private val blastArray = ArrayList<String>()

    private var blastImagePathPosition = 0
    private var shouldDestroy = false

    init {
        (1..32).forEach {
            val element = "/img/blast_$it.png"
            blastArray.add(element)
        }
    }

    override fun draw() {
        if (blastImagePathPosition < blastArray.size) {
            Painter.drawImage(blastArray[blastImagePathPosition], positionX, positionY)
            blastImagePathPosition++
        } else {
            shouldDestroy = true
        }
    }

    override fun shouldDestroy(): Boolean {
        return shouldDestroy
    }

}