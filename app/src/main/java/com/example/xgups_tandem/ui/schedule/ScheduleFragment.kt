package com.example.xgups_tandem.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.databinding.FragmentScheduleBinding
import com.example.xgups_tandem.ui.schedule.dayadapter.DayAdapter

class ScheduleFragment : Fragment() {


    private var _binding: FragmentScheduleBinding? = null
    private val binding
        get() = _binding!!

    val viewModel by viewModels<ScheduleViewModel>()

    private val dayAdapter = DayAdapter()
    private val lessonAdapter = ScheduleLessonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        dayAdapterConfigure()
        lessonAdapterConfigure()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.imageProfile.load("https://sun7-13.userapi.com/impg/z2nfUVtnV_hFAaFEeN_oC0A7Iig22BRk1uXn_w/vKNF080jniU.jpg?size=1215x2160&quality=95&sign=cd8d8575e08036e92d9f79ce5a3b99cb&type=album") {
            crossfade(true)
            placeholder(R.mipmap.user)
            transformations(CircleCropTransformation())
        }
    }

    /** Отдаем ссылку адаптера ресайклу, связываем коллекции [lessonAdapter] с [ScheduleViewModel] */
    private fun lessonAdapterConfigure() {
        lessonAdapter.lessonList = viewModel.lessonList.value!!
        binding.lessonsRecycler.adapter = lessonAdapter
    }

    /** Отдаем ссылку адаптера ресайклу, связываем коллекции [dayAdapter] с [ScheduleViewModel], реагируем на нажатие элемента с ресайклера. */
    private fun dayAdapterConfigure() {
        binding.daysRecycler.adapter = dayAdapter
        dayAdapter.setListOnAdapter(viewModel.dateList.value!!)
        dayAdapter.setOnClickListner {  }
    }
}