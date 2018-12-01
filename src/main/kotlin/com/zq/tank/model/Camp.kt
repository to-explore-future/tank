package com.zq.tank.model

import com.zq.tank.interF.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

class Camp(override var positionX: Int, override var positionY: Int) : Obstructive, Beatable, AutoDestroy {

    override var blood: Int = 12

    override var width: Int = Config.block * 2
    override var height: Int = Config.block + 32

    private var noWallX: Int = positionX + 32
    private var noWallY: Int = positionY + 32
    private var noWallWidth = Config.block
    private var noWallHeight = Config.block

    override fun draw() {

        if (blood <= 1) {
            positionX = noWallX
            positionY = noWallY
            width = noWallWidth
            height = noWallHeight
            Painter.drawImage("/img/camp.gif", positionX, positionY)
        } else if (blood <= 6) {
            drawOuterWall("/img/wall_small.gif")
        } else {
            drawOuterWall("/img/steel_small.gif")
        }
    }

    private fun drawOuterWall(outerWallImagePath: String) {
        Painter.drawImage(outerWallImagePath, positionX, positionY)
        Painter.drawImage(outerWallImagePath, positionX + 32, positionY)
        Painter.drawImage(outerWallImagePath, positionX + 32 * 2, positionY)
        Painter.drawImage(outerWallImagePath, positionX + 32 * 3, positionY)

        Painter.drawImage(outerWallImagePath, positionX, positionY + 32)
        Painter.drawImage(outerWallImagePath, positionX, positionY + 32 * 2)

        Painter.drawImage(outerWallImagePath, positionX + 32 * 3, positionY + 32)
        Painter.drawImage(outerWallImagePath, positionX + 32 * 3, positionY + 32 * 2)

        Painter.drawImage("/img/camp.gif", positionX + 32, positionY + 32)
    }

    override fun notifyBeat(attackObj: AttackAbility): Array<View>? {
        blood -= attackObj.atk

        if (blood == 6) { //钢变成墙 周围爆炸
            return returnBlasts()
        }
        if (blood == 1) {
            return returnBlasts()
        }

        Composer.play("sound/hit.wav")
        return null
    }

    private fun returnBlasts(): Array<View> {
        val blast1 = Blast(positionX + 32 * -1, positionY + 32 * -1, 32, 32)
        val blast2 = Blast(positionX + 32 * 0, positionY + 32 * -1, 32, 32)
        val blast3 = Blast(positionX + 32 * 1, positionY + 32 * -1, 32, 32)
        val blast4 = Blast(positionX + 32 * 2, positionY + 32 * -1, 32, 32)
        val blast5 = Blast(positionX + 32 * 3, positionY + 32 * -1, 32, 32)

        val blast6 = Blast(positionX + 32 * -1, positionY + 32 * 0, 32, 32)
        val blast7 = Blast(positionX + 32 * -1, positionY + 32 * 1, 32, 32)
        val blast8 = Blast(positionX + 32 * -1, positionY + 32 * 2, 32, 32)

        val blast9 = Blast(positionX + 32 * 3, positionY + 32 * 0, 32, 32)
        val blast10 = Blast(positionX + 32 * 3, positionY + 32 * 1, 32, 32)
        val blast11 = Blast(positionX + 32 * 3, positionY + 32 * 2, 32, 32)


        return arrayOf(blast1, blast2, blast3, blast4, blast5, blast6, blast7, blast8, blast9, blast10, blast11)
    }

    override fun shouldDestroy(): Boolean = blood < 0

    override fun showDestrory(): Array<View>? {

        val blast1 = Blast(positionX + 32 * -1, positionY + 32 * -1, 32, 32)
        val blast2 = Blast(positionX + 32 * 0, positionY + 32 * -1, 32, 32)
        val blast3 = Blast(positionX + 32 * 1, positionY + 32 * -1, 32, 32)

        val blast4 =  Blast(positionX + 32 * -1, positionY + 32 * 0, 32, 32)
        val blast5 =  Blast(positionX + 32 * -1, positionY + 32 * 1, 32, 32)

        val blast6 = Blast(positionX + 32 * 1, positionY + 32 * 0, 32, 32)
        val blast7 = Blast(positionX + 32 * 1, positionY + 32 * 1, 32, 32)

        return arrayOf(blast1, blast2, blast3, blast4, blast5, blast6, blast7)
    }
}