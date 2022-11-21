package com.f0x1d.epos.utils.store

import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.UnauthorizedException
import com.f0x1d.epos.network.model.response.AuthResponse
import com.f0x1d.epos.network.okhttp.EposCookieJar
import com.f0x1d.epos.utils.toObjFromJson
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object OkHttpClientStore {

    private var client: OkHttpClient? = null
    private var cookieJar: EposCookieJar? = null

    private val mutex = Mutex()

    @Throws(Exception::class)
    suspend fun requireClient(recreate: Boolean = false) = withContext(Dispatchers.Default) {
        return@withContext mutex.withLock {
            if (recreate) {
                client = null
                cookieJar = null
            }

            if (client == null) {
                cookieJar = EposCookieJar()

                client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(cookieJar!!)
                    .build()

                withContext(Dispatchers.IO) {
                    processAuth()
                }
            }

            return@withLock client!!
        }
    }

    @Throws(Exception::class)
    private fun processAuth() {
        val token = EposApplication.appPreferences.askToken()
        if (token == null || token.isEmpty())
            throw UnauthorizedException()

        try {
            val request = Request.Builder()
                .url("https://school.permkrai.ru/authenticate")
                .header("token", token)
                .build()

            val response = client!!
                .newCall(request)
                .execute()
                .body?.run {
                    val result = string().toObjFromJson(AuthResponse::class.java)
                    close()
                    return@run result
                }

            if (response != null) {
                EposApplication.appPreferences.saveToken(response.mobileToken)

                cookieJar?.apply {
                    addCookie("aid", response.cookies.aid)
                    EposApplication.appPreferences.saveAid(response.cookies.aid)

                    addCookie("auth_token", response.cookies.authToken)
                    addCookie("from_sudir", response.cookies.fromSudir.toString())
                    addCookie("is_auth", response.cookies.isAuth.toString())
                    addCookie("kc_token", "null")
                    addCookie("long_token", response.cookies.longToken)

                    addCookie("profile_id", response.cookies.profileId)
                    EposApplication.appPreferences.saveProfileId(response.cookies.profileId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnauthorizedException()
        }
    }
}