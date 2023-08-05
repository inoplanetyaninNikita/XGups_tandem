package com.example.xgups_tandem.ui.schedule.bottomSheetDialog

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.base.BaseBottomSheetDialogFragment
import com.example.xgups_tandem.databinding.FragmentBooksBinding
import com.example.xgups_tandem.ui.schedule.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.io.path.Path

@AndroidEntryPoint
class BooksFragment : BaseBottomSheetDialogFragment<FragmentBooksBinding>(FragmentBooksBinding::inflate) {
    private val adapter = BookAdapter()
    private val viewModel by viewModels<BooksViewModel>()

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
        viewModel.fileToOpen.observe(viewLifecycleOwner){
            openPdfFile(requireContext(),it)
        }
    }

    override fun setListeners() {

        adapter.setOnClickListner {
            viewModel.downloadFile(
                it.fileurl + "&token="+Moodle.token,
                Path(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .absolutePath, it.filename).toString()
            )
        }
    }

    fun openPdfFile(context: Context, filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(uri, "application/pdf")
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            // Обработка ситуации, когда на устройстве нет приложения для просмотра PDF
        }
    }






}