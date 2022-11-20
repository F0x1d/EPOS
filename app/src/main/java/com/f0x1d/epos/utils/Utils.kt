package com.f0x1d.epos.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.f0x1d.epos.R
import com.f0x1d.epos.network.model.response.LessonResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


val Number.toPx get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

private val gson = Gson()

fun <T> String?.toObjFromJson(clazz: Class<T>): T {
    return gson.fromJson(this, clazz)
}
fun <T> String?.toObjFromJson(type: TypeToken<T>): T {
    return gson.fromJson(this, type.type)
}

fun getColorFromAttr(c: Context, @AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    c.theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

fun SwipeRefreshLayout.setupColors() {
    setColorSchemeColors(getColorFromAttr(context, R.attr.colorPrimary))
    setProgressBackgroundColorSchemeColor(getColorFromAttr(context, R.attr.colorSurface))
}

fun weekBounds(offset: Int = 0): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_WEEK, 7 * offset)

    val weekStart = calendar.run {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        time
    }
    val weekEnd = calendar.run {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        time
    }
    return Pair(weekStart, weekEnd)
}
fun weekDays(offset: Int = 0): List<Date> {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_WEEK, 7 * offset)

    return mutableListOf<Date>().apply {
        for (i in 1..7) {
            add(calendar.run {
                set(Calendar.DAY_OF_WEEK, if (i == 7) 1 else i + 1)
                time
            })
        }
    }
}
fun Date.toDayDate(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault()).format(this)

fun Int.mapWeight() = when (this) {
    1 -> "¹"
    2 -> "²"
    3 -> "³"
    4 -> "⁴"
    5 -> "⁵"
    else -> this
}

fun LessonResponse?.genHash(default: Long = 0): Long {
    if (this == null) return default

    return ((dateArray[0] shl 32) + (dateArray[1] shl 16) + dateArray[2]).toLong()
}