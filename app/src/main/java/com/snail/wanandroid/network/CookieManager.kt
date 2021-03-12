package com.snail.wanandroid.network

import com.snail.wanandroid.utils.SharePreferencesUtils

open class CookieManager {
    private var cookies = ""

    fun saveCookie(cookies: List<String>) {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            .map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            .forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        SharePreferencesUtils.instance.cookie =sb.toString()
    }

    fun getCookie(): String  =  SharePreferencesUtils.instance.cookie

    fun cleanCookie(){
        SharePreferencesUtils.instance.cookie = ""
    }

//    private fun encodeCookies(cookies: List<String>): String {
//
//    }
}