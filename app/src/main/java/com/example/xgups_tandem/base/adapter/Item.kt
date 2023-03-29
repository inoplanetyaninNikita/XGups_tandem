package com.example.xgups_tandem.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

sealed class Item<T : Any, VB : ViewBinding>(
    val clazz: KClass<T>,
    val vbInflater: (LayoutInflater, ViewGroup, Boolean) -> VB
) {
    class BaseItem<T : Any, VB : ViewBinding>(
        clazz: KClass<T>,
        vbInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        val bind: ViewHolder.BaseViewHolder<VB>.(T) -> Unit
    ) : Item<T, VB>(clazz, vbInflater)

    class CustomItem<T : Any, VB : ViewBinding, VH : ViewHolder>(
        clazz: KClass<T>,
        vbInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        val holder: (VB) -> VH
    ) : Item<T, VB>(clazz, vbInflater)
}