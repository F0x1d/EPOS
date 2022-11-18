package com.f0x1d.epos.adapter.lessoninfo

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.base.BaseAdapter
import com.f0x1d.epos.network.model.response.LessonResponse
import java.util.*

class TimeAdapter(): BaseAdapter<LessonResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LessonResponse> = TimeViewHolder(inflateView(parent, R.layout.item_time))

    class TimeViewHolder(itemView: View) : BaseViewHolder<LessonResponse>(itemView) {

        private val timeBounds: TextView = itemView.findViewById(R.id.time_bounds)
        private val dateName: TextView = itemView.findViewById(R.id.date_name)
        private val cabinetName: TextView = itemView.findViewById(R.id.cabinet_name)

        @SuppressLint("SetTextI18n")
        override fun bindTo(t: LessonResponse) {
            timeBounds.text = "${t.startTime}\n${t.endTime}"
            dateName.text = Calendar.getInstance().run {
                set(Calendar.YEAR, t.dateArray[0])
                set(Calendar.MONTH, t.dateArray[1] - 1)
                set(Calendar.DAY_OF_MONTH, t.dateArray[2])
                t.dateArray[2].toString() + " " +
                        getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                        ", " +
                        getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            }
            cabinetName.text = t.roomName
        }

        override fun recycle() {
        }
    }
}