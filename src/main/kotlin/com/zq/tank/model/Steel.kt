package com.zq.tank.model

import com.zq.tank.interF.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter


class Steel(override var positionX: Int, override var positionY: Int) : Obstructive, Beatable {
    override var blood: Int = 20


    override val width: Int = Config.block
    override val height: Int = Config.block

    override fun draw() {
        Painter.drawImage("/image/steel.png", positionX, positionY)
    }

    override fun notifyBeat(attackObj: AttackAbility): Array<View> {
        Composer.play("sound/hit.wav")
        //暂时不掉血
        return getBlast(attackObj)
    }

}