package com.example.xgups_tandem.ui.grades.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemGradeBinding
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class GradeAdapter: BaseListAdapter<SamGUPS.MarkResponse>()
{
    @SuppressLint("ResourceAsColor")
    override fun build() {
        baseViewHolder(SamGUPS.MarkResponse::class, ItemGradeBinding::inflate){
            binding.run {
                binding.tvName.text = it.name
                binding.tvMark.text = it.mark


                when(it.mark) {
                    "Отлично"->  {
                        ivMark.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.mipmap.badgecheck))
                        ivMark.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.xgpefecto), android.graphics.PorterDuff.Mode.SRC_IN)
                        tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.xgpefecto))
                    }
                    "Хорошо" ->  {
                        ivMark.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.mipmap.badgecheck))
                        ivMark.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.xgpefecto), android.graphics.PorterDuff.Mode.SRC_IN)
                        tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.xgpefecto))
                    }
                    "Зачтено" ->  {
                        ivMark.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.mipmap.badgecheck))
                        ivMark.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.xgpefecto), android.graphics.PorterDuff.Mode.SRC_IN)
                        tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.xgpefecto))
                    }
                    "Удовлетворительно" -> {
                        ivMark.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.mipmap.badgecheck))
                        ivMark.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.xgwarning), android.graphics.PorterDuff.Mode.SRC_IN)
                        tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.xgwarning))
                    }
                    else -> {
                        ivMark.setImageDrawable(ContextCompat.getDrawable(binding.root.context,R.mipmap.exclamation))
                        ivMark.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.xgfail), android.graphics.PorterDuff.Mode.SRC_IN)
                        tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.xgfail))
                    }
                }

            }
        }
    }

}