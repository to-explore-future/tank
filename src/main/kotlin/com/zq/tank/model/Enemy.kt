package com.zq.tank.model

import com.zq.tank.emum.Direction
import com.zq.tank.interF.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import java.util.*


class Enemy(override var positionX: Int, override var positionY: Int) :
        AutoMovable, Movable, Obstructive, AutoShoot, Beatable, AutoDestroy {

    override var blood: Int = 3

    override var currentDirection: Direction = Direction.DOWN

    override var direction: Direction = Direction.DOWN
    override val speed: Int = 8

    private val maxPositionX = Config.gameWidth - Config.block
    private val maxPositionY = Config.gameHeight - Config.block

    private var badDirection: Direction? = null

    private val interval: Long = 50
    private var lastTime: Long = 0


    override fun autoMove() {

        //自动移动之前 要判断当前这个方向是不是 badDirection
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastTime > interval) {
            if (direction == badDirection) {
                direction = changeDirection(badDirection)
                badDirection = null
                return
            }

            //移动之前要 判断是否会碰撞
            if (isAutoMove) {
                when (direction) {
                    Direction.LEFT -> {
                        positionX -= speed
                    }
                    Direction.UP -> {
                        positionY -= speed
                    }
                    Direction.RIGHT -> {
                        positionX += speed
                    }
                    Direction.DOWN -> {
                        positionY += speed
                    }
                }
            }


            //越界判断
            if (positionX < 0) {
                positionX = 0
                badDirection = Direction.LEFT
                return
            }
            if (positionX > maxPositionX) {
                positionX = maxPositionX
                badDirection = Direction.RIGHT
                return
            }
            if (positionY < 0) {
                positionY = 0
                badDirection = Direction.UP
                return
            }
            if (positionY > maxPositionY) {
                positionY = maxPositionY
                badDirection = Direction.DOWN
                return
            }
            lastTime = System.currentTimeMillis()
        }

    }

    override fun checkCollision(view: View): Boolean {

        var x = positionX
        var y = positionY

        when (direction) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.RIGHT -> x += speed
            Direction.LEFT -> x -= speed
        }
        return when {
            view.positionY + view.height <= y -> false
            view.positionX + view.width <= x -> false
            y + height <= view.positionY -> false
            x + width <= view.positionX -> false
            else -> {
                true
            }
        }
    }

    override fun draw() {
        val tankPath = when (direction) {
            Direction.LEFT -> "/img/enemy_1_l.gif"
            Direction.UP -> "/img/enemy_1_u.gif"
            Direction.RIGHT -> "/img/enemy_1_r.gif"
            Direction.DOWN -> "/img/enemy_1_d.gif"
        }
        Painter.drawImage(tankPath, positionX, positionY)

    }

    override fun willCollision(obstruct: Obstructive): Direction? {
        val isCollision = checkCollision(obstruct)
        if (!isCollision) {  //如果不碰撞 那么就可以自动移动
            isAutoMove = true
        }
        return if (isCollision) direction else null
    }

    private var isAutoMove = false
    override fun notifyCollision(badDirection: Direction?, obstruct: Obstructive?) {
        this.badDirection = badDirection
//        val changeDirection = changeDirection(badDirection)
//        direction = changeDirection
        //方向改变之后禁止 自动移动
        isAutoMove = false
    }

    private fun changeDirection(badDirection: Direction?): Direction {
        val nextInt = Random().nextInt(4)
        val direction = when (nextInt) {
            0 -> Direction.UP
            1 -> Direction.RIGHT
            2 -> Direction.DOWN
            3 -> Direction.LEFT
            else -> return Direction.DOWN
        }
        return if (direction == badDirection) {
            changeDirection(direction)
        } else {
            direction
        }
    }

    private val shootInterval = 1500
    private var shootLastTime = 0L


    override fun autoShoot(): Bullet? {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - shootLastTime > shootInterval) {
            shootLastTime = currentTimeMillis
            return Bullet(this, direction) { bulletWidth, bulletHeight ->
                val bulletX: Int
                val bulletY: Int
                when (direction) {

                    Direction.UP -> {
                        bulletX = positionX + (width - bulletWidth) / 2
                        bulletY = positionY - bulletHeight / 2
                        Pair(bulletX, bulletY)
                    }

                    Direction.LEFT -> {
                        bulletX = positionX - bulletWidth / 2
                        bulletY = positionY + (height - bulletHeight) / 2
                        Pair(bulletX, bulletY)
                    }

                    Direction.RIGHT -> {
                        bulletX = positionX + width - bulletWidth / 2
                        bulletY = positionY + (height - bulletHeight) / 2
                        Pair(bulletX, bulletY)
                    }

                    Direction.DOWN -> {
                        bulletX = positionX + (width - bulletWidth) / 2
                        bulletY = positionY + height - bulletHeight / 2
                        Pair(bulletX, bulletY)
                    }
                }
            }
        }
        return null
    }

    override fun notifyBeat(attackObj: AttackAbility): Array<View> {
        if (attackObj.owner !is Enemy) blood -= attackObj.atk
        Composer.play("sound/hit.wav")

        return getBlast(attackObj)
    }

    override fun shouldDestroy(): Boolean {
        return blood <= 0
    }
}