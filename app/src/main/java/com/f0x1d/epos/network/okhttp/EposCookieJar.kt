package com.f0x1d.epos.network.okhttp

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class EposCookieJar: CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> = cookies.filter { it.matches(url) }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.addAll(cookies)
    }

    fun addCookie(name: String, value: String) {
        cookies.add(
            Cookie.Builder().apply {
                name(name)
                value(value)
                domain("school.permkrai.ru")
            }.build()
        )
    }
}