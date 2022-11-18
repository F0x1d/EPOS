package com.f0x1d.epos.network

import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.R
import com.f0x1d.epos.network.model.response.ProfileGroup
import com.f0x1d.epos.utils.store.OkHttpClientStore
import com.f0x1d.epos.utils.toDayDate
import com.f0x1d.epos.utils.toObjFromJson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException
import java.util.*

fun profileUrl(profileId: String?, aid: String?) = "https://school.permkrai.ru/core/api/student_profiles/$profileId?" +
        "academic_year_id=$aid&" +
        "with_attendance=true&" +
        "with_ec_attendance=true&" +
        "with_ae_attendance=true&" +
        "with_parents=false&" +
        "with_subjects=true&" +
        "with_lesson_info=true&" +
        "with_final_marks=true&" +
        "with_archived_groups=true"

fun lessonsUrl(weekBounds: Pair<Date, Date>, profileId: String?, groups: List<ProfileGroup>) = "https://school.permkrai.ru/jersey/api/schedule_items?" +
        "per_page=100&" +
        "from=${weekBounds.first.toDayDate("yyyy-MM-dd")}&" +
        "to=${weekBounds.second.toDayDate("yyyy-MM-dd")}&" +
        "student_profile_id=$profileId&" +
        "group_id=${groups.map { it.id }.joinToString("%2C")}&" +
        "with_lesson_info=1&" +
        "with_course_calendar_info=true&" +
        "with_group_class_subject_info=true&" +
        "with_rooms_info=true"

fun marksUrl(weekBounds: Pair<Date, Date>, profileId: String?) = "https://school.permkrai.ru/core/api/marks?" +
        "created_at_from=${weekBounds.first.toDayDate("dd.MM.yyyy")}&" +
        "created_at_to=${weekBounds.second.toDayDate("dd.MM.yyyy")}&" +
        "student_profile_id=$profileId&" +
        "page=0&" +
        "per_page=1000"

fun homeworkUrl(weekBounds: Pair<Date, Date>, profileId: String?) = "https://school.permkrai.ru/core/api/student_homeworks?" +
        "student_profile_id=$profileId&" +
        "page=0&" +
        "per_page=200&" +
        "begin_date=${weekBounds.first.toDayDate("dd.MM.yyyy")}&" +
        "end_date=${weekBounds.second.toDayDate("dd.MM.yyyy")}&" +
        "begin_prepared_date=${weekBounds.first.toDayDate("dd.MM.yyyy")}&" +
        "end_prepared_date=${weekBounds.second.toDayDate("dd.MM.yyyy")}"

fun allMarksUrl(profileId: String?, aid: String?) = "https://school.permkrai.ru/reports/api/progress/json?" +
        "student_profile_id=$profileId&" +
        "academic_year_id=$aid"



@Throws(Exception::class)
suspend fun <T> OkHttpClient.makeSignedRequest(url: String, type: TypeToken<T>): T? {
    val request = Request.Builder()
        .url(url)
        .build()

    val response = try {
        newCall(request)
            .execute()
    } catch (e: Exception) {
        if (e is SocketTimeoutException) {
            throw UnauthorizedException(R.string.timeout_unauthorized)
        }
        throw e
    }

    if (response.code == 401 || response.code == 400) {
        throw UnauthorizedException()
    } else if (response.code != 200) {
        throw Exception(response.code.toString() + "\n" + response.body?.string().toString())
    }

    return response.body?.run {
        val result = string().toObjFromJson(type)
        close()
        return@run result
    }
}

class UnauthorizedException(msg: Int = R.string.unauthorized): Exception(EposApplication.instance.getString(msg))