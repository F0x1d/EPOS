package com.f0x1d.epos.adapter.marks

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.MarksPeriod

class PeriodAdapter(): BaseAdapter<MarksPeriod>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MarksPeriod> = PeriodViewHolder(inflateView(parent, R.layout.item_period_title))

    class PeriodViewHolder(itemView: View) : BaseViewHolder<MarksPeriod>(itemView) {

        private val periodName: TextView = itemView.findViewById(R.id.period_name)

        @SuppressLint("SetTextI18n")
        override fun bindTo(t: MarksPeriod) {
            periodName.text = "${t.name}: ${t.average}"

            if (t.finalMark != null)
                periodName.text = periodName.text.toString() + ", " + periodName.context.getString(R.string.final_mark, t.finalMark)
        }

        override fun recycle() {

        }
    }
}