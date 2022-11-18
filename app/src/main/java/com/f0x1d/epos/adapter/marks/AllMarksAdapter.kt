package com.f0x1d.epos.adapter.marks

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.AllMarksResponse
import com.f0x1d.epos.network.model.response.Mark
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class AllMarksAdapter(private val onMarkClicked: (Mark) -> Unit): BaseAdapter<AllMarksResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AllMarksResponse> = MarksViewHolder(inflateView(parent, R.layout.item_marks))

    inner class MarksViewHolder(itemView: View): BaseViewHolder<AllMarksResponse>(itemView) {

        private val subjectName: TextView = itemView.findViewById(R.id.subject_name)
        private val marksRecycler: RecyclerView = itemView.findViewById(R.id.marks_recycler)

        init {
            marksRecycler.layoutManager = object : FlexboxLayoutManager(marksRecycler.context, FlexDirection.ROW) {
                override fun canScrollVertically(): Boolean = false
            }
        }

        override fun bindTo(t: AllMarksResponse) {
            subjectName.text = t.subjectName

            val adapter = ConcatAdapter()
            t.periods.forEach {
                adapter.addAdapter(PeriodAdapter().apply { elements = listOf(it) })
                adapter.addAdapter(MarksAdapter(onMarkClicked).apply { elements = it.marks })
            }

            marksRecycler.adapter = adapter
        }

        override fun recycle() {
        }
    }
}