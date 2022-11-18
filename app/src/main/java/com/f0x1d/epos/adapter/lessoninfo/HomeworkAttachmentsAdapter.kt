package com.f0x1d.epos.adapter.lessoninfo

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.HomeworkAttachment

class HomeworkAttachmentsAdapter : BaseAdapter<HomeworkAttachment>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeworkAttachment> =
        HomeworkAttachmentViewHolder(inflateView(parent, R.layout.item_attachment_homework))

    class HomeworkAttachmentViewHolder(itemView: View): BaseViewHolder<HomeworkAttachment>(itemView) {

        private val attachmentLayout: ConstraintLayout = itemView.findViewById(R.id.attachment_layout)
        private val fileName: TextView = itemView.findViewById(R.id.file_name)

        init {
            attachmentLayout.setOnClickListener {
                attachmentLayout.context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://school.permkrai.ru${(bindingAdapter as HomeworkAttachmentsAdapter).elements[bindingAdapterPosition].path}")
                })
            }
        }

        override fun bindTo(t: HomeworkAttachment) {
            fileName.text = t.fileName
        }

        override fun recycle() {
        }
    }
}