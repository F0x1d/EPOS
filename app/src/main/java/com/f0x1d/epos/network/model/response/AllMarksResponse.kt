package com.f0x1d.epos.network.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AllMarksResponse(@SerializedName("subject_name") val subjectName: String,
                            @SerializedName("periods") val periods: List<MarksPeriod>)

@Keep
data class MarksPeriod(@SerializedName("name") val name: String,
                       @SerializedName("marks") val marks: List<Mark>,
                       @SerializedName("avg_original") val average: String,
                       @SerializedName("start_iso") val start: String,
                       @SerializedName("end_iso") val end: String,
                       @SerializedName("final_mark") val finalMark: String?)

@Keep
data class Mark(@SerializedName("values") val values: List<MarkValues>,
                @SerializedName("weight") val weight: Int,
                @SerializedName("control_form_name") val controlFormName: String,
                @SerializedName("topic_name") val topicName: String,
                @SerializedName("date") val date: String)

@Keep
data class MarkValues(@SerializedName("original") val original: String)