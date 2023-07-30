package com.example.xgups_tandem.ui.schedule.bottomSheetDialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.base.BaseBottomSheetDialogFragment
import com.example.xgups_tandem.databinding.FragmentBooksBinding


class BooksFragment : BaseBottomSheetDialogFragment<FragmentBooksBinding>(FragmentBooksBinding::inflate) {

    private val adapter = BookAdapter()

    val books: MutableLiveData<Array<Moodle.Content>> by lazy {
        MutableLiveData<Array<Moodle.Content>>()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            books.value = it.getParcelableArray("books") as? Array<Moodle.Content>
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun setAdapter() {
        super.setAdapter()
        binding.rvBooks.adapter =  adapter
        books.value.let {
            if (it != null) {
                adapter.submitList(it.toList())
            }
        }

    }

    override fun setObservable() {
        books.observe(viewLifecycleOwner){
            adapter.submitList(it.toList())
        }

    }

}