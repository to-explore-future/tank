package com.zq.tank.model

import com.zq.tank.emum.Direction
import com.zq.tank.interF.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

class Wall(override var positionX: Int, override var positionY: Int) : Obstructive, Beatable, AutoDestroy {

    override var blood: Int = 3

    override val width: Int = Config.block
    override val height: Int = Config.block

    override fun draw() {
        Painter.drawImage("/image/wall.png", positionX, positionY)
    }

    override fun notifyBeat(attackObj: AttackAbility): Array<View> {
        blood -= attackObj.atk
        Composer.play("sound/hit.wav")

        return getBlast(attackObj)

    }

    override fun shouldDestroy(): Boolean = blood <= 0


}