package com.zq.tank.interF

import com.zq.tank.emum.Direction

interface AutoMovable : View, CollisionAbility {

    val direction: Direction

    val speed: Int

    //自动移动
    fun autoMove()

}