package com.example.xgups_tandem.ui.help

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AbsListView.OnScrollListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentHelpgptBinding

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
        binding.backArrowProfile2.setOnClickListener{
            onBackPressed()
        }
        binding.rvChat.addOnLayoutChangeListener{
                view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int ->

            viewModel.messages.value?.let { binding.rvChat.scrollToPosition(it.lastIndex) }

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
        viewModel.messages.value?.let { binding.rvChat.scrollToPosition(it.lastIndex) }
    }
    private fun urlOpen(url : String){
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(urlIntent)
    }


}