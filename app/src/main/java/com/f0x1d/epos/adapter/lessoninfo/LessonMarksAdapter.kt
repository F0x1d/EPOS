package com.f0x1d.epos.adapter.lessoninfo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.LessonResponse

class LessonMarksAdapter: BaseAdapter<LessonResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LessonResponse> = MarkViewHolder(inflateView(parent, R.layout.item_marks_block))

    class MarkViewHolder(itemView: View) : BaseViewHolder<LessonResponse>(itemView) {

        private val markTitle: TextView = itemView.findViewById(R.id.title)
        private val markText: TextView = itemView.findViewById(R.id.text)

        override fun bindTo(t: LessonResponse) {
            markTitle.setText(R.string.marks)
            markText.text = t.parseMarks()
        }

        override fun recycle() {
        }
    }
}