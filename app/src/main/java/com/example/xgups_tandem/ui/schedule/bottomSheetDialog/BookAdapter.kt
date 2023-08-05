package com.example.xgups_tandem.ui.schedule.bottomSheetDialog

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemBookBinding
import com.example.xgups_tandem.databinding.ItemGradeBinding
import com.example.xgups_tandem.ui.schedule.lessonAdapter.LessonModel
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import kotlin.io.path.Path

class BookAdapter : BaseListAdapter<Moodle.Content>() {
    private var actionOnClick: ((Moodle.Content) -> Unit)? = null

    fun setOnClickListner(action: ((Moodle.Content) -> Unit)?) {
        actionOnClick = action
    }

    override fun build() {
        baseViewHolder(Moodle.Content::class, ItemBookBinding::inflate) {
            binding.run {
                binding.tvFile.text = it.filename
                val content = it

                binding.root.setOnClickListener {
                    actionOnClick?.invoke(content)
                }
            }
        }
    }
}
