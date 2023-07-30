package com.example.xgups_tandem.ui.schedule.bottomSheetDialog

import androidx.core.content.ContextCompat
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemBookBinding
import com.example.xgups_tandem.databinding.ItemGradeBinding

class BookAdapter : BaseListAdapter<Moodle.Content>() {
    override fun build() {
        baseViewHolder(Moodle.Content::class, ItemBookBinding::inflate){
            binding.run {
                binding.tvFile.text = it.filename
            }
        }
    }
}