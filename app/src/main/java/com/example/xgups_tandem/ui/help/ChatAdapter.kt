package com.example.xgups_tandem.ui.help

import android.opengl.Visibility
import android.view.Gravity.RIGHT
import android.view.View
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemMessageBinding
import com.google.android.material.transition.MaterialSharedAxis.Axis

class ChatAdapter: BaseListAdapter<Message>() {

    override fun build() {
        baseViewHolder(Message::class, ItemMessageBinding::inflate){

            binding.tvChatMessage.text = it.text

            //Gravity + username
            if(it.itsMe){
                binding.tvChatUsername.text = "Вы"

                binding.root.gravity = RIGHT
                binding.tvChatUsername.gravity = RIGHT
            } else {
                binding.tvChatUsername.text = "XGPT 3.5"
            }

            //Скрываем кнопку, если ссылка пустая
            if(it.url == ""){
                binding.cvChatURL.visibility = View.GONE
            }
            binding.root.setOnClickListener{
            }
        }
    }
}