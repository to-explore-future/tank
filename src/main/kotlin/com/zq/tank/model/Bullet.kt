package com.zq.tank.model

import com.zq.tank.interF.*
import com.zq.tank.interF.View
import com.zq.tank.emum.Direction
import org.itheima.kotlin.game.core.Painter

class Bullet(override val owner: View, override val direction: Direction,
             getBulletXY: (bulletWidth: Int, bulletHeight: Int) -> Pair<Int, Int>) :
        AutoMovable, AutoDestroy, AttackAbility, Obstructive, Beatable {
    override var blood: Int = 1

    override val atk: Int = 1

    override val speed: Int = 8

    override var positionX: Int = 0
    override var positionY: Int = 0

    override val width: Int
    override val height: Int

    private val bulletPath: String = when (direction) {
        Direction.UP -> "/bullet/bullet_up.png"
        Direction.RIGHT -> "/bullet/bullet_right.png"
        Direction.DOWN -> "/bullet/bullet_down.png"
        Direction.LEFT -> "/bullet/bullet_left.png"
    }

    private var shouldDestroy: Boolean = false

    init {
        val size = Painter.size(bulletPath)
        width = size[0]
        height = size[1]
        val pair = getBulletXY.invoke(width, height)
        positionX = pair.first
        positionY = pair.second

    }

    override fun draw() {
        Painter.drawImage(bulletPath, positionX, positionY)
    }

    override fun autoMove() {
        when (direction) {
            Direction.UP -> positionY -= speed
            Direction.DOWN -> positionY += speed
            Direction.LEFT -> positionX -= speed
            Direction.RIGHT -> positionX += speed
        }
    }

    override fun shouldDestroy(): Boolean {

        if (blood < 1) return true
        if (shouldDestroy) return true
        if (positionX < -width) return true
        if (positionX > Config.gameWidth) return true
        if (positionY > Config.gameHeight) return true
        if (positionY < -height) return true
        return false
    }

    override fun isCollision(beatObj: Beatable): Boolean {
        return checkCollision(beatObj)
    }

    override fun checkCollision(view: View): Boolean {
        if ((this.owner is Enemy) and (view is Enemy)) { //地方的子弹 不能攻击敌方的坦克
            return false
        }
        return when {
            view.positionY + view.height <= positionY -> false
            view.positionX + view.width <= positionX -> false
            positionY + height <= view.positionY -> false
            positionX + width <= view.positionX -> false
            else -> true
        }
    }

    override fun notifyAttack(beatObj: Beatable) {
        shouldDestroy = true
    }

    override fun notifyBeat(attackObj: AttackAbility): Array<View>? {
        var x: Int = positionX
        var y: Int = positionY
        //保证爆炸物的爆炸中心 在两个子弹相撞的中心
        when (direction) {
            Direction.UP -> {
                x = positionX + width / 2 - Config.block / 2
                y = positionY - Config.block
            }
            Direction.LEFT -> {
                x = positionX - Config.block / 2
                y = positionY + height / 2 - Config.block / 2
            }

            Direction.RIGHT -> {
                x = positionX + width - Config.block / 2
                y = positionY + height / 2 - Config.block / 2
            }
            Direction.DOWN -> {
                x = positionX + width / 2 - Config.block / 2
                y = positionY + height - Config.block / 2
            }

        }
        return arrayOf(Blast(x, y))
    }
}