package com.zq.tank.interF

import com.zq.tank.model.Bullet

/**
 * 自动开火的能力
 */
interface AutoShoot {

    fun autoShoot() :Bullet?

}