package com.zq.tank.model

import com.zq.tank.emum.Direction
import com.zq.tank.interF.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

class Tank(override var positionX: Int, override var positionY: Int) :
        Movable, Beatable, AutoDestroy, Obstructive {


    private var tankPath = "/tanks/my_tank_up.png"

    override val width: Int = Config.block
    override val height: Int = Config.block

    override var speed: Int = 8
    override var currentDirection: Direction = Direction.UP
    private val maxPositionX = Config.gameWidth - Config.block
    private val maxPositionY = Config.gameHeight - Config.block
    private var badDirection: Direction? = null



    override fun draw() {
        tankPath = when (this.currentDirection) {
            Direction.LEFT -> {
                "/tanks/my_tank_left.png"
            }
            Direction.UP -> {
                "/tanks/my_tank_up.png"
            }
            Direction.RIGHT -> {
                "/tanks/my_tank_right.png"
            }
            Direction.DOWN -> {
                "/tanks/my_tank_down.png"
            }
        }
        Painter.drawImage(tankPath, positionX, positionY)
    }

    private val badDirectionUpdateTimeInterval = 50
    private var lastBadDirectionUpdateTime = 0L

    fun move(direction: Direction) {

        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastBadDirectionUpdateTime > badDirectionUpdateTimeInterval) {
            badDirection = null
        }

        if (direction == badDirection) {
            println("碰撞方向")
            return
        }

        if (this.currentDirection != direction) {
            println("this.currentDirection != direction")
            this.currentDirection = direction
            return
        }

        println("朝着某个方向移动了")

        when (direction) {
            Direction.LEFT -> {
                positionX -= speed
                tankPath = "/tanks/my_tank_left.png"
            }
            Direction.UP -> {
                positionY -= speed
                tankPath = "/tanks/my_tank_up.png"
            }
            Direction.RIGHT -> {
                positionX += speed
                tankPath = "/tanks/my_tank_right.png"
            }
            Direction.DOWN -> {
                positionY += speed
                tankPath = "/tanks/my_tank_down.png"
            }
        }

        //只要是经过了 上面的位置发生变化 说明以前的badDirection 已经失效了
        badDirection = null

        this.currentDirection = direction

        if (positionX < 0) {
            positionX = 0
            return
        }
        if (positionX > maxPositionX) {
            positionX = maxPositionX
            return
        }
        if (positionY < 0) {
            positionY = 0
            return
        }
        if (positionY > maxPositionY) {
            positionY = maxPositionY
            return
        }
    }

    override fun willCollision(obstruct: Obstructive): Direction? {
        val collision = checkCollision(obstruct)
        return if (collision) currentDirection else null
    }

    override fun checkCollision(view: View): Boolean {
        var x = positionX
        var y = positionY

        when (currentDirection) {
            Direction.LEFT -> x -= speed
            Direction.UP -> y -= speed
            Direction.RIGHT -> x += speed
            Direction.DOWN -> y += speed
        }

        return when {
            view.positionY + view.height <= y -> false
            view.positionX + view.width <= x -> false
            y + height <= view.positionY -> false
            x + width <= view.positionX -> false
            else -> true
        }
    }

    override fun notifyCollision(badDirection: Direction?, obstruct: Obstructive?) {
        this.badDirection = badDirection
        println("badDirection = ${this.badDirection}")
        lastBadDirectionUpdateTime = System.currentTimeMillis()

    }

    private val shootInterval = 100
    private var lastShootTime = 0L

    fun shoot(): Bullet? {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastShootTime > shootInterval) {
            lastShootTime = currentTimeMillis
            return Bullet(this, currentDirection) { bulletWidth, bulletHeight ->
                val bulletX: Int
                val bulletY: Int
                when (currentDirection) {


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

    override var blood: Int = 15

    override fun notifyBeat(attackObj: AttackAbility): Array<View> {
        blood -= attackObj.atk
        Composer.play("sound/hit.wav")
        return getBlast(attackObj)
    }

    override fun shouldDestroy(): Boolean {
        return blood <= 0
    }


}