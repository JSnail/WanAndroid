package com.snail.wanandroid.network

class CookieManager {
    private var cookies = ""

    fun saveCookie(cookies: List<String>) {
        cookies.forEach{
            this.cookies +=it
        }
    }

    fun getCookie(): String  = cookies

//    private fun encodeCookies(cookies: List<String>): String {
//
//    }
}