package com.f0x1d.epos.adapter.marks

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.Mark
import com.f0x1d.epos.utils.mapWeight

class MarksAdapter(private val onMarkClicked: (Mark) -> Unit): BaseAdapter<Mark>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Mark> = MarkViewHolder(inflateView(parent, R.layout.item_mark))

    inner class MarkViewHolder(itemView: View): BaseViewHolder<Mark>(itemView) {

        private val markLayout: ConstraintLayout = itemView.findViewById(R.id.mark_layout)
        private val mark: TextView = itemView.findViewById(R.id.mark)

        init {
            markLayout.setOnClickListener {
                onMarkClicked.invoke((bindingAdapter as MarksAdapter).elements[bindingAdapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindTo(t: Mark) {
            mark.text = t.values.firstOrNull()?.original + t.weight.mapWeight()
        }

        override fun recycle() {
        }
    }
}