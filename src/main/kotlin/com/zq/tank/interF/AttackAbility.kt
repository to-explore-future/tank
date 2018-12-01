package com.zq.tank.interF

/**
 * 攻击的能力
 * 攻击力
 */
interface AttackAbility : View {

    val owner : View

    val atk :Int //攻击力

    //是否碰撞
    fun isCollision(beatObj: Beatable): Boolean

    //攻击通知
    fun notifyAttack(beatObj: Beatable)

}