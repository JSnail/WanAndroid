package com.snail.wanandroid.network


/**
 * @Author  Snail
 * @Date 12/30/20
 * @Description
 **/
interface Function2<T1, T2, R> {
    fun apply(t1: T1, t2: T2): R
}