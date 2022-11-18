package com.f0x1d.epos.network.model.response

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.f0x1d.epos.utils.mapWeight
import com.google.gson.annotations.SerializedName

@Keep
data class MarkResponse(@SerializedName("created_at") val createdAt: String,
                        @SerializedName("id") val id: Long,
                        @SerializedName("teacher_id") val teacherId: Long,
                        @SerializedName("weight") val weight: Int,
                        @SerializedName("name") val name: String,
                        @SerializedName("comment") val comment: String?,
                        @SerializedName("is_point") val isPoint: Boolean,
                        @SerializedName("point_date") val pointDate: String?,
                        @SerializedName("schedule_lesson_id") val scheduleLessonId: Long): Parcelable {

    override fun toString(): String {
        return if (!isPoint) toMark()
        else "• (${toMark()})"
    }

    fun toMark() = name + weight.mapWeight()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createdAt)
        parcel.writeLong(id)
        parcel.writeLong(teacherId)
        parcel.writeInt(weight)
        parcel.writeString(name)
        parcel.writeString(comment)
        parcel.writeByte(if (isPoint) 1 else 0)
        parcel.writeString(pointDate)
        parcel.writeLong(scheduleLessonId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarkResponse> {
        override fun createFromParcel(parcel: Parcel): MarkResponse {
            return MarkResponse(parcel)
        }

        override fun newArray(size: Int): Array<MarkResponse?> {
            return arrayOfNulls(size)
        }
    }
}