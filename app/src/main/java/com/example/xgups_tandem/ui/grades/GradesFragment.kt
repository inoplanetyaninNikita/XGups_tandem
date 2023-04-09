package com.example.xgups_tandem.ui.grades

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentGradesBinding
import com.example.xgups_tandem.ui.grades.adapter.GradeAdapter

class GradesFragment : BaseFragment<FragmentGradesBinding>(FragmentGradesBinding::inflate) {
    val viewModel by viewModels<GradesViewModel>()
    val adapter = GradeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun setAdapter() {
        binding.rv.adapter = adapter;

        val values : Array<String> = arrayOf("USD", "UAH", "GBD", "EUR", "BIT", "RUB")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, values)
        binding.spinner.adapter = adapter
    }
    override fun setObservable() {
        binding.names.text = "${mainViewModel.user.value?.secondName} ${mainViewModel.user.value?.firstName}"
        binding.group.text = mainViewModel.user.value?.bookNumber
        binding.ivGrade.load("https://sun7-13.userapi.com/impg/z2nfUVtnV_hFAaFEeN_oC0A7Iig22BRk1uXn_w/vKNF080jniU.jpg?size=1215x2160&quality=95&sign=cd8d8575e08036e92d9f79ce5a3b99cb&type=album") {
            crossfade(true)
            placeholder(R.mipmap.user)
            transformations(CircleCropTransformation())
        }



        adapter.submitList(mainViewModel.marks.value)
        mainViewModel.marks.observe(viewLifecycleOwner){
            adapter.submitList(mainViewModel.marks.value)
        }
    }
}