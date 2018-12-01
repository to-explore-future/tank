package com.zq.tank.interF

import com.zq.tank.emum.Direction

interface Movable : View, CollisionAbility {

    var currentDirection: Direction

    val speed: Int  //注意运动物体的速度 一定要是 Config.block的基数

    fun willCollision(obstruct: Obstructive): Direction?

    fun notifyCollision(badDirection: Direction?, obstruct: Obstructive?)

}