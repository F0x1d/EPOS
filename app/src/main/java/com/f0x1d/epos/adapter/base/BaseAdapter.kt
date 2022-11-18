package com.f0x1d.epos.adapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>>() {

    var elements = emptyList<T>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindTo(elements[position])
    }

    override fun getItemCount(): Int = elements.size

    override fun onViewRecycled(holder: BaseViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    protected fun inflateView(parent: ViewGroup, id: Int): View = LayoutInflater.from(parent.context).inflate(id, parent, false)

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindTo(t: T)
        abstract fun recycle()
    }
}