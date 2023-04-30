package com.example.xgups_tandem.ui.help

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.xgups_tandem.api.convertClassToJson
import com.example.xgups_tandem.api.convertJsonToClass
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentHelpgptBinding
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class HelpFragment : BaseFragment<FragmentHelpgptBinding>(FragmentHelpgptBinding::inflate) {
    val viewModel by viewModels<HelpViewModel>()
    val adapter = ChatAdapter()

    override fun setAdapter() {
        viewModel.messages

        adapter.submitList(viewModel.messages.value)
        binding.rvChat.adapter = adapter

        viewModel.messages.observe(viewLifecycleOwner){
            updateList()
        }
    }
    override fun setListeners() {
        binding.btnSubmit.setOnClickListener{
            sendMessage()
        }
    }
    override fun setObservable() {

    }

    private fun sendMessage(){
        viewModel.sendMessage(binding.etChat.text.toString())
        binding.etChat.setText("")
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun updateList() {
        adapter.notifyDataSetChanged()
    }
    private fun urlOpen(url : String){
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(urlIntent)
    }


}