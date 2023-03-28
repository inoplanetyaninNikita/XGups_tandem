package com.example.xgups_tandem.ui.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.MainActivity
import com.example.xgups_tandem.MainViewModel
import com.example.xgups_tandem.R
import com.example.xgups_tandem.databinding.FragmentScheduleBinding
import com.example.xgups_tandem.ui.schedule.dayadapter.DayAdapter
import java.text.Format
import java.text.Normalizer.Form

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding
        get() = _binding!!

    lateinit var activityViewModel : MainViewModel
    private val viewModel by viewModels<ScheduleViewModel>()


    private val dayAdapter = DayAdapter()
    private val lessonAdapter = ScheduleLessonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        setListners()
        dayAdapterConfigure()
        lessonAdapterConfigure()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.name.value = arguments?.getString("first_name") + " " + arguments?.getString("second_name")
        viewModel.image.value = "https://sun7-13.userapi.com/impg/z2nfUVtnV_hFAaFEeN_oC0A7Iig22BRk1uXn_w/vKNF080jniU.jpg?size=1215x2160&quality=95&sign=cd8d8575e08036e92d9f79ce5a3b99cb&type=album"

    }

    /** Отдаем ссылку адаптера ресайклу, связываем коллекции [lessonAdapter] с [ScheduleViewModel] */
    private fun lessonAdapterConfigure() {
        lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
        binding.lessonsRecycler.adapter = lessonAdapter

    }

    /** Отдаем ссылку адаптера ресайклу, связываем коллекции [dayAdapter] с [ScheduleViewModel], реагируем на нажатие элемента с ресайклера. */
    private fun dayAdapterConfigure() {
        binding.daysRecycler.adapter = dayAdapter
        dayAdapter.setListOnAdapter(viewModel.dateList.value!!)
    }
    private fun setListners()
    {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.names.text  = binding.root.context.resources.getString(R.string.schedule_names,it)
        }

        viewModel.image.observe(viewLifecycleOwner) {
            binding.imageProfile.load(it) {
                crossfade(true)
                placeholder(R.mipmap.user)
                transformations(CircleCropTransformation())
            }
        }

        viewModel.lessonList.observe(viewLifecycleOwner) {
            lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
        }

        dayAdapter.setOnClickListner {
            viewModel.clear()
        }
    }
}