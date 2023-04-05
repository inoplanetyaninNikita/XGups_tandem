package com.example.xgups_tandem.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BasePagerAdapter<T : Fragment>(
    private val items: Array<T>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount() = items.size
    override fun createFragment(position: Int) = items[position]
}