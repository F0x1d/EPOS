package com.f0x1d.epos.adapter

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.LessonResponse

class LessonsAdapter(private val click: (LessonResponse) -> Unit) : BaseAdapter<LessonResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LessonResponse> = LessonViewHolder(inflateView(parent, R.layout.item_lesson), click)

    class LessonViewHolder(itemView: View, private val click: (LessonResponse) -> Unit) : BaseViewHolder<LessonResponse>(itemView) {

        private val lessonLayout: ConstraintLayout = itemView.findViewById(R.id.lesson_layout)
        private val timeBounds: TextView = itemView.findViewById(R.id.time_bounds)
        private val lessonName: TextView = itemView.findViewById(R.id.subject_name)
        private val homeworkIndicator: ImageView = itemView.findViewById(R.id.homework_indicator)
        private val marksText: TextView = itemView.findViewById(R.id.marks_text)

        init {
            lessonLayout.setOnClickListener {
                click.invoke((bindingAdapter as LessonsAdapter).elements[bindingAdapterPosition])
            }
        }

        override fun bindTo(t: LessonResponse) {
            lessonName.text = t.subjectName
            timeBounds.text = "${t.startTime}\n${t.endTime}"

            val homeworkText = t.homeworks.firstOrNull()?.homeworkEntry?.description ?: ""
            val allMarksText = t.parseMarks()

            marksText.text = if (allMarksText.isNotEmpty())
                Html.fromHtml("<b>$allMarksText</b>")
            else
                homeworkText

            homeworkIndicator.visibility = if (t.homeworks.isNotEmpty()) View.VISIBLE else View.GONE
            marksText.visibility = if (marksText.text.isNotEmpty()) View.VISIBLE else View.GONE
        }

        override fun recycle() {
        }
    }
}