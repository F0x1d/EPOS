package com.f0x1d.epos.network.repository

import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.*
import com.f0x1d.epos.network.model.response.*
import com.f0x1d.epos.network.repository.base.BaseRepository
import com.f0x1d.epos.utils.store.OkHttpClientStore
import com.f0x1d.epos.utils.weekBounds
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.*
import kotlin.collections.HashMap

class LessonsRepository: BaseRepository<HashMap<Int, List<LessonResponse>>>() {

    private var offset = 0

    override fun updateValues(values: Array<Any>): BaseRepository<HashMap<Int, List<LessonResponse>>> {
        offset = values[0] as Int
        return super.updateValues(values)
    }

    override suspend fun requestData(): HashMap<Int, List<LessonResponse>> = withContext(Dispatchers.IO) {
        val client = OkHttpClientStore.requireClient()
        val weekBounds = weekBounds(offset)

        lateinit var profile: ProfileResponse
        val lessons = mutableListOf<LessonResponse>()
        val groups = mutableListOf<ProfileGroup>()
        val marks = mutableListOf<MarkResponse>()
        val homeworks = mutableListOf<HomeworkResponse>()

        val profileTask = async {
            val response = client.makeSignedRequest(
                profileUrl(
                    EposApplication.appPreferences.askProfileId(),
                    EposApplication.appPreferences.askAid()
                ), object : TypeToken<ProfileResponse>() {})?.also {
                profile = it
            }?.groups

            groups.addAll(response ?: emptyList())

            val lessonsResponse = client.makeSignedRequest(
                lessonsUrl(
                    weekBounds,
                    EposApplication.appPreferences.askProfileId(),
                    groups
                ), object : TypeToken<List<LessonResponse>>() {})

            lessons.addAll(lessonsResponse ?: emptyList())
        }

        val marksTask = async {
            val response = client.makeSignedRequest(
                marksUrl(
                    weekBounds,
                    EposApplication.appPreferences.askProfileId()
                ), object : TypeToken<List<MarkResponse>>() {})

            marks.addAll(response ?: emptyList())
        }

        val homeworkTask = async {
            val response = client.makeSignedRequest(
                homeworkUrl(
                    weekBounds,
                    EposApplication.appPreferences.askProfileId()
                ), object : TypeToken<List<HomeworkResponse>>() {})

            homeworks.addAll(response ?: emptyList())
        }

        profileTask.await()
        marksTask.await()
        homeworkTask.await()

        lessons.forEach { lesson ->
            lesson.parseTimes()

            lesson.marks = marks.filter { it.scheduleLessonId == lesson.id }
            lesson.homeworks = homeworks
                .filter {
                    lesson.homeworksToVerify
                        ?.map { it.id }
                        ?.contains(it.homeworkEntry.homework.id) == true
                }

            lesson.attended = !profile.attendances.any { it.scheduleLessonId == lesson.id }
        }

        return@withContext HashMap<Int, List<LessonResponse>>().apply {
            for (i in 1..7) {
                val lessonsThisDay = lessons
                    .filter { i == it.dayNumber }
                    .sortedBy { it.studyOrdinal }

                put(i, lessonsThisDay)
            }
        }
    }
}