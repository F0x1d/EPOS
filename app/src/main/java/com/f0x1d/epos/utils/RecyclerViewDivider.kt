package com.f0x1d.epos.utils

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.roundToInt

// a little bit modified divider
class RecyclerViewDivider(context: Context): ItemDecoration() {
    /**
     * @return the [Drawable] for this divider.
     */
    var drawable: Drawable?
        private set
    var orientation: Int = RecyclerView.VERTICAL
    private val mBounds = Rect()
    private val margins = 0.toPx.toInt()
    var shouldDraw: (Int, Int) -> Boolean = { current, max -> current != max - 1 }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || drawable == null) {
            return
        }
        if (orientation == RecyclerView.VERTICAL)
            drawVertical(c, parent)
        else
            drawHorizontal(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (!shouldDraw.invoke(i, childCount)) continue

            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + child.translationY.roundToInt()
            val top = bottom - drawable!!.intrinsicHeight
            drawable!!.setBounds(left + margins, top, right - margins, bottom)
            drawable!!.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (!shouldDraw.invoke(i, childCount)) continue

            val child = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + child.translationX.roundToInt()
            val left: Int = right - drawable!!.intrinsicWidth
            drawable!!.setBounds(left, top + margins, right, bottom - margins)
            drawable!!.draw(canvas)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (drawable == null) {
            outRect[0, 0, 0] = 0
            return
        }
        outRect[0, 0, 0] = drawable!!.intrinsicHeight
    }

    companion object {
        private const val TAG = "DividerItem"
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }

    /**
     * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
     * [LinearLayoutManager].
     *
     * @param context Current context, it will be used to access resources.
     */
    init {
        val a = context.obtainStyledAttributes(ATTRS)
        drawable = a.getDrawable(0)
        if (drawable == null) {
            Log.w(
                TAG, "@android:attr/listDivider was not set in the theme used for this "
                        + "DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
    }
}