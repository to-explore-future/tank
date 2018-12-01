package com.zq.tank.interF

import com.zq.tank.emum.Direction
import com.zq.tank.model.Blast
import com.zq.tank.model.Bullet

/**
 * 被打击的能力，能被打的就有血量，就能够被销毁
 */
interface Beatable : View {

    var blood: Int

    //被攻击了
    fun notifyBeat(attackObj: AttackAbility): Array<View>?

    //获取爆炸物
    fun getBlast(attackObj: AttackAbility): Array<View> {
        attackObj as Bullet
        var x = attackObj.positionX
        var y = attackObj.positionY

        when (attackObj.direction) {
            Direction.UP -> {
                x = attackObj.positionX + attackObj.width / 2 - Config.block / 2
                y = attackObj.positionY - Config.block / 2
            }
            Direction.DOWN -> {
                x = attackObj.positionX + attackObj.width / 2 - Config.block / 2
                y = attackObj.positionY + attackObj.height - Config.block / 2
            }
            Direction.LEFT -> {
                x = attackObj.positionX - Config.block / 2
                y = attackObj.positionY + attackObj.height / 2 - Config.block / 2
            }
            Direction.RIGHT -> {
                x = attackObj.positionX + attackObj.width - Config.block / 2
                y = attackObj.positionY + attackObj.height / 2 - Config.block / 2
            }
        }
        return arrayOf(Blast(x, y))
    }

}