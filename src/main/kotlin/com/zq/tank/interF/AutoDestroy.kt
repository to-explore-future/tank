package com.zq.tank.interF

/**
 * 自动销毁的能力
 */
interface AutoDestroy {

    //是否自动销毁
    fun shouldDestroy(): Boolean

    fun showDestrory() : Array<View>?{
        return null
    }
}