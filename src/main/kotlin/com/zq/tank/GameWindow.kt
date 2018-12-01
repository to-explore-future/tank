package com.zq.tank

import com.zq.tank.interF.*
import com.zq.tank.emum.Direction
import com.zq.tank.model.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Window
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow : Window("坦克大战", "/tanks/my_tank_up.png", Config.gameWidth, Config.gameHeight) {

    private val views = CopyOnWriteArrayList<View>()
    private val bornLocations = CopyOnWriteArrayList<Pair<Int, Int>>()

    private lateinit var tank: Tank

    override fun onCreate() {

        Composer.play("sound/start.wav")
        val file = File(javaClass.getResource("/map/1.map").file)

        val readLines = file.readLines(Charset.defaultCharset())
        var line = 0
        readLines.forEach { mapElement ->

            val charArray = mapElement.toCharArray()
            var row = 0
            charArray.forEach {

                when (it) {
                    '草' -> views.add(Grass(row * Config.block, line * Config.block))
                    '水' -> views.add(Water(row * Config.block, line * Config.block))
                    '钢' -> views.add(Steel(row * Config.block, line * Config.block))
                    '墙' -> views.add(Wall(row * Config.block, line * Config.block))
                    '敌' -> bornLocations.add(Pair(row * Config.block, line * Config.block))
                }
                row++
            }
            line++
        }
        //创建tank
        tank = Tank(8 * Config.block, 12 * Config.block)
        views.add(tank)

        //创建大本营
        val camp = Camp((Config.gameWidth / 2 - (Config.block / 2 + 32)), (Config.gameHeight - Config.block - 32))
        views.add(camp)
    }

    override fun onDisplay() {
//        println(views.size)
        views.forEach {
            it.draw()
        }
    }

    override fun onKeyPressed(event: KeyEvent) {

        if (isGameOver) return
        when (event.code) {
            KeyCode.W -> tank.move(Direction.UP)
            KeyCode.A -> tank.move(Direction.LEFT)
            KeyCode.S -> tank.move(Direction.DOWN)
            KeyCode.D -> tank.move(Direction.RIGHT)
            KeyCode.ENTER -> {
                val shoot = tank.shoot()
                shoot?.let { views.add(shoot) }

            }
            else -> {
            }
        }
    }

    private var enemyTotalSize = 20
    private var bornEnemySize = 0
    private var activeEnemySize = 10
    private var index = 0

    private var isGameOver = false

    override fun onRefresh() {

        if (isGameOver) return

        //碰撞检测
        views.filter { it is Movable }.forEach { move ->
            move as Movable
            var badDirection: Direction?
            var badObstruct: Obstructive?
            views.filter { (it is Obstructive) and (it != move) and (it !is Bullet) }.forEach obstruct@{ obstruct ->
                obstruct as Obstructive
                val direction = move.willCollision(obstruct)
                direction?.let {
                    badDirection = direction
                    badObstruct = obstruct
                    move.notifyCollision(badDirection, badObstruct)
                    return@obstruct
                }
            }
        }

        //自动移动 ：坦克移动 子弹飞行
        views.filter { it is AutoMovable }.forEach { autoMoveObject ->
            (autoMoveObject as AutoMovable).autoMove()
        }

        //销毁子弹
        for (view in views.filter { it is AutoDestroy }) {
            if ((view as AutoDestroy).shouldDestroy()) {
                views.remove(view)
                val blasts = view.showDestrory()
                blasts?.let {
                    views.addAll(blasts)
                }
            }
        }

        //子弹碰撞检测
        views.filter { it is AttackAbility }.forEach { attackObj ->
            attackObj as AttackAbility
            views.filter { (it is Beatable) and (attackObj.owner != it) and (it != attackObj) }.forEach beatObjectFlag@{ beatObj ->
                beatObj as Beatable
                if (attackObj.isCollision(beatObj)) {
                    attackObj.notifyAttack(beatObj)
                    val beats = beatObj.notifyBeat(attackObj)
                    beats?.let {
                        views.addAll(beats)
                    }
                    return@beatObjectFlag
                }
            }
        }


        views.filter { it is AutoShoot }.forEach { autoShootObj ->
            autoShootObj as AutoShoot
            val bullet = autoShootObj.autoShoot()
            bullet?.let {
                views.add(bullet)
            }
        }

        //游戏结束
        if ((views.none { it is Camp }) or ((bornEnemySize >= enemyTotalSize) and (views.none { it is Enemy }))) {
            isGameOver = true
        }


        if ((views.filter { it is Enemy }.size < activeEnemySize)) {

            var isNewEnemyShouldBorn = true
            val position = index % bornLocations.size
            println("position:$position")
            val pair = bornLocations[position]
            println("pair")
            //检测 地图中的 tanks 是否在这个区域 x：pair.first y:pair.second width:Config.block height:Config.block
            views.filter { it is Enemy }.forEach enemyOnBirthPoint@{
                val isEnemyOnBirthPoint = checkCollision(pair, it)
                if (isEnemyOnBirthPoint) {
                    //只要有一个敌人在出生点 那么就选择下一个出生点
                    isNewEnemyShouldBorn = false
                    return@enemyOnBirthPoint //结束循环
                }
            }

            //如果能走到这里说明 没有一个敌人在出生点
            if (isNewEnemyShouldBorn and (bornEnemySize < enemyTotalSize)) {
                views.add(Enemy(pair.first, pair.second))
                bornEnemySize++
                println("敌人出生地  x:${pair.first} y:${pair.second}")
            }
            index++
        }
    }

    //检测出生点 有没有其他的坦克
    private fun checkCollision(pair: Pair<Int, Int>, view: View): Boolean {
        val x = pair.first
        val y = pair.second
        return when {
            y + Config.block <= view.positionY -> false
            x + Config.block <= view.positionX -> false
            view.positionX + view.width <= x -> false
            view.positionY + view.height <= y -> false
            else -> {
                true
            }
        }
    }

}