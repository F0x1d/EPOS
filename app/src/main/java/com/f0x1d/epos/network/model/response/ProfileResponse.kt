package com.f0x1d.epos.network.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileResponse(@SerializedName("id") val id: Long,
                           @SerializedName("user_name") val userName: String,
                           @SerializedName("groups") val groups: List<ProfileGroup>,
                           @SerializedName("attendances") val attendances: List<ProfileAttendance>)

@Keep
data class ProfileAttendance(@SerializedName("schedule_lesson_id") val scheduleLessonId: Long)

@Keep
data class ProfileGroup(@SerializedName("id") val id: Long,
                        @SerializedName("name") val name: String)