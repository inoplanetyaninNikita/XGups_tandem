package com.example.xgups_tandem.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

sealed class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        ViewHolder(binding.root)

    abstract class CustomViewHolder<T : Any>(root: View) : ViewHolder(root) {
        abstract fun bind(item: T)
    }
}