package com.example.xgups_tandem.ui.help

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.util.DisplayMetrics
import android.view.Gravity.RIGHT
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemMessageBinding
import com.google.android.material.transition.MaterialSharedAxis.Axis

class ChatAdapter: BaseListAdapter<Message>() {


    override fun build() {
        baseViewHolder(Message::class, ItemMessageBinding::inflate){
            val message = it

            val displayMetrics =  DisplayMetrics()

//            binding.tvChatMessage.maxWidth =  displayMetrics.widthPixels

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
                urlOpen(message.url, binding.root.context)
            }
        }
    }

    private fun urlOpen(url : String, context: Context){
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(context, urlIntent, null)
    }
}