package com.keithsmyth.androidshowcase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Simple ListAdapter implementation. Trades some manual casting for simplicity.
 * Good starting point for iterative improvements.
 */
class BindingAdapter : ListAdapter<BindingItem, BindingViewHolder>(BindingItemDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = getItem(position)
        item.bind(holder.itemView)
    }
}

class BindingViewHolder(layout: View) : ViewHolder(layout)

/**
 * Item that can be returned by ViewModel. Encapsulates the binding code,
 * and can have onClick functions passed into it.
 */
interface BindingItem {

    /**
     * Unique layoutRes is treated as the ItemViewType in ListAdapter.
     */
    @get:LayoutRes
    val layoutRes: Int

    /**
     * Manually create ViewBinding, then use model data in class implementation.
     */
    fun bind(layout: View)

    /**
     * Compare primary key for DiffUtil.ItemCallback.
     * Can assume the newItem argument can be safely cast to same implementation.
     */
    fun hasSamePrimaryKey(newItem: BindingItem): Boolean

    /**
     * Compare entire class for DiffUtil.ItemCallback.
     * Can assume the newItem argument can be safely cast to same implementation.
     */
    fun hasSameContents(newItem: BindingItem): Boolean
}

private class BindingItemDiffUtil : DiffUtil.ItemCallback<BindingItem>() {
    override fun areItemsTheSame(oldItem: BindingItem, newItem: BindingItem): Boolean {
        return oldItem.hasSamePrimaryKey(newItem)
    }

    override fun areContentsTheSame(oldItem: BindingItem, newItem: BindingItem): Boolean {
        return oldItem.hasSameContents(newItem)
    }
}
