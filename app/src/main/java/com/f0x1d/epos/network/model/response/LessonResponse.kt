package com.f0x1d.epos.network.model.response

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.f0x1d.epos.utils.toDayDate
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
data class LessonResponse(@SerializedName("id") val id: Long,
                          @SerializedName("teacher_id") val teacherId: Long,
                          @SerializedName("subject_name") val subjectName: String,
                          @SerializedName("lesson_name") val lessonName: String,
                          @SerializedName("duration") val duration: Int,
                          @SerializedName("date") val dateArray: IntArray,
                          @SerializedName("time") val timeArray: IntArray,
                          @SerializedName("iso_date_time") val isoDateTime: String,
                          @SerializedName("day_number") val dayNumber: Int,
                          @SerializedName("study_ordinal") val studyOrdinal: Int,
                          @SerializedName("room_name") val roomName: String,
                          @SerializedName("homeworks_to_give") val homeworksToGive: List<LessonHomework>?,
                          @SerializedName("homeworks_to_verify") val homeworksToVerify: List<LessonHomework>?,
                          var attended: Boolean = true,
                          var startTime: String? = null,
                          var endTime: String? = null,
                          var marks: List<MarkResponse> = emptyList(),
                          var homeworks: List<HomeworkResponse> = emptyList()): Parcelable {

    fun showMarks() = marks.isNotEmpty() || !attended

    fun parseMarks(): String {
        var allMarksText = marks.joinToString(", ")
        if (!attended) allMarksText = "Н" + if (allMarksText.isEmpty()) "" else ", $allMarksText"
        return allMarksText
    }

    fun parseTimes() {
        val calendar = Calendar.getInstance()
        startTime = calendar.run {
            set(Calendar.HOUR_OF_DAY, timeArray[0])
            set(Calendar.MINUTE, timeArray[1])
            set(Calendar.SECOND, timeArray[2])
            time.toDayDate("HH:mm")
        }
        endTime = calendar.run {
            add(Calendar.MINUTE, duration)
            time.toDayDate("HH:mm")
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createIntArray()!!,
        parcel.createIntArray()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(LessonHomework),
        parcel.createTypedArrayList(LessonHomework),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(MarkResponse)!!,
        parcel.createTypedArrayList(HomeworkResponse)!!
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LessonResponse) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(teacherId)
        parcel.writeString(subjectName)
        parcel.writeString(lessonName)
        parcel.writeInt(duration)
        parcel.writeIntArray(dateArray)
        parcel.writeIntArray(timeArray)
        parcel.writeString(isoDateTime)
        parcel.writeInt(dayNumber)
        parcel.writeInt(studyOrdinal)
        parcel.writeString(roomName)
        parcel.writeTypedList(homeworksToGive)
        parcel.writeTypedList(homeworksToVerify)
        parcel.writeByte(if (attended) 1 else 0)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeTypedList(marks)
        parcel.writeTypedList(homeworks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LessonResponse> {
        override fun createFromParcel(parcel: Parcel): LessonResponse {
            return LessonResponse(parcel)
        }

        override fun newArray(size: Int): Array<LessonResponse?> {
            return arrayOfNulls(size)
        }
    }
}

@Keep
data class LessonHomework(@SerializedName("id") val id: Long): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readLong())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
    }

    companion object CREATOR : Parcelable.Creator<LessonHomework> {
        override fun createFromParcel(parcel: Parcel): LessonHomework {
            return LessonHomework(parcel)
        }

        override fun newArray(size: Int): Array<LessonHomework?> {
            return arrayOfNulls(size)
        }
    }
}