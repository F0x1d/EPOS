package com.f0x1d.epos.adapter.lessoninfo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.HomeworkResponse
import com.f0x1d.epos.utils.toPx

class HomeworkAdapter: BaseAdapter<HomeworkResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeworkResponse> = HomeworkViewHolder(inflateView(parent, R.layout.item_homework_block))

    class HomeworkViewHolder(itemView: View): BaseViewHolder<HomeworkResponse>(itemView) {

        private val homeworkTitle: TextView = itemView.findViewById(R.id.title)
        private val homeworkText: TextView = itemView.findViewById(R.id.text)
        private val attachmentsRecycler: RecyclerView = itemView.findViewById(R.id.recycler)

        init {
            attachmentsRecycler.layoutManager = object : LinearLayoutManager(attachmentsRecycler.context) {
                override fun canScrollVertically(): Boolean = false
            }
            attachmentsRecycler.adapter = HomeworkAttachmentsAdapter()
        }

        override fun bindTo(t: HomeworkResponse) {
            homeworkTitle.setText(if (t.homeworkEntry.homework.classRoomJob) R.string.classwork else R.string.homework)
            homeworkText.text = t.homeworkEntry.description

            val hasAttachments = t.homeworkEntry.attachments.isNotEmpty()

            attachmentsRecycler.visibility = if (hasAttachments) View.VISIBLE else View.GONE
            (attachmentsRecycler.adapter as HomeworkAttachmentsAdapter).elements = t.homeworkEntry.attachments

            (homeworkText.layoutParams as ConstraintLayout.LayoutParams).apply {
                bottomMargin = if (hasAttachments) 5.toPx.toInt() else 15.toPx.toInt()
            }
        }

        override fun recycle() {
        }
    }
}