package com.pokhodai.marvel.base.ui.fragments

import androidx.recyclerview.widget.DiffUtil

@Suppress("UNCHECKED_CAST")
fun <T> getEqualsDiff() = EqualsDiff as DiffUtil.ItemCallback<T>

private object EqualsDiff : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Any, newItem: Any) =
        areItemsTheSame(oldItem, newItem)
}