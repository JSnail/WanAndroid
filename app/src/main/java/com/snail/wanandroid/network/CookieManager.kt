package com.snail.wanandroid.network

import android.util.Log

class CookieManager {
    private var cookies = ""

    fun saveCookie(cookies: List<String>) {
        cookies.forEach{
            this.cookies +=it
        }
        Log.i("TAG","cookies == $cookies")
    }

    fun getCookie(): String  = cookies

//    private fun encodeCookies(cookies: List<String>): String {
//
//    }
}