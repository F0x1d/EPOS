package com.f0x1d.epos.network.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AuthResponse(@SerializedName("mobile_token") val mobileToken: String,
                        @SerializedName("cookies") val cookies: AuthCookies)

@Keep
data class AuthCookies(@SerializedName("long_token") val longToken: String,
                       @SerializedName("auth_token") val authToken: String,
                       @SerializedName("aid") val aid: String,
                       @SerializedName("profile_id") val profileId: String,
                       @SerializedName("is_auth") val isAuth: Boolean,
                       @SerializedName("from_sudir") val fromSudir: Boolean)
