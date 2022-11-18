package com.f0x1d.epos.network.model.response

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HomeworkResponse(@SerializedName("id") val id: Long,
                            @SerializedName("created_at") val createdAt: String,
                            @SerializedName("homework_entry_id") val homeworkEntryId: Long,
                            @SerializedName("homework_entry") val homeworkEntry: HomeworkEntry): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readParcelable(HomeworkEntry::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(createdAt)
        parcel.writeLong(homeworkEntryId)
        parcel.writeParcelable(homeworkEntry, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeworkResponse> {
        override fun createFromParcel(parcel: Parcel): HomeworkResponse {
            return HomeworkResponse(parcel)
        }

        override fun newArray(size: Int): Array<HomeworkResponse?> {
            return arrayOfNulls(size)
        }
    }

}

@Keep
data class HomeworkEntry(@SerializedName("id") val id: Long,
                         @SerializedName("homework_id") val homeworkId: Long,
                         @SerializedName("description") val description: String,
                         @SerializedName("duration") val duration: Int?,
                         @SerializedName("homework") val homework: Homework,
                         @SerializedName("attachments") val attachments: List<HomeworkAttachment>): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Homework::class.java.classLoader)!!,
        parcel.createTypedArrayList(HomeworkAttachment)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(homeworkId)
        parcel.writeString(description)
        parcel.writeValue(duration)
        parcel.writeParcelable(homework, flags)
        parcel.writeTypedList(attachments)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeworkEntry> {
        override fun createFromParcel(parcel: Parcel): HomeworkEntry {
            return HomeworkEntry(parcel)
        }

        override fun newArray(size: Int): Array<HomeworkEntry?> {
            return arrayOfNulls(size)
        }
    }

}

@Keep
data class Homework(@SerializedName("id") val id: Long,
                    @SerializedName("is_class_room_job") val classRoomJob: Boolean): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeByte(if (classRoomJob) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Homework> {
        override fun createFromParcel(parcel: Parcel): Homework {
            return Homework(parcel)
        }

        override fun newArray(size: Int): Array<Homework?> {
            return arrayOfNulls(size)
        }
    }

}

@Keep
data class HomeworkAttachment(@SerializedName("id") val id: Long,
                              @SerializedName("file_file_name") val fileName: String,
                              @SerializedName("path") val path: String,
                              @SerializedName("file_file_size") val size: Long): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(fileName)
        parcel.writeString(path)
        parcel.writeLong(size)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeworkAttachment> {
        override fun createFromParcel(parcel: Parcel): HomeworkAttachment {
            return HomeworkAttachment(parcel)
        }

        override fun newArray(size: Int): Array<HomeworkAttachment?> {
            return arrayOfNulls(size)
        }
    }

}