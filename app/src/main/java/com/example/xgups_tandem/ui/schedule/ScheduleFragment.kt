package com.example.xgups_tandem.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.MainViewModel
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentLoginBinding
import com.example.xgups_tandem.databinding.FragmentScheduleBinding
import com.example.xgups_tandem.ui.login.LoginFragmentDirections
import com.example.xgups_tandem.ui.schedule.dayadapter.DayAdapter
import com.example.xgups_tandem.ui.schedule.lessonAdapter.ScheduleLessonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate)  {
    private val viewModel by viewModels<ScheduleViewModel>()

    private val dayAdapter = DayAdapter()
    private val lessonAdapter = ScheduleLessonAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.value = "https://sun7-13.userapi.com/impg/z2nfUVtnV_hFAaFEeN_oC0A7Iig22BRk1uXn_w/vKNF080jniU.jpg?size=1215x2160&quality=95&sign=cd8d8575e08036e92d9f79ce5a3b99cb&type=album"
        viewModel.name.value = "${mainViewModel.user.value!!.secondName} ${mainViewModel.user.value!!.firstName}"
        viewModel.group.value = mainViewModel.user.value!!.group
    }

    override fun setAdapter() {
        dayAdapter.setListOnAdapter(viewModel.dateList.value!!)
        binding.daysRecycler.adapter = dayAdapter

        lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
        binding.lessonsRecycler.adapter = lessonAdapter
        binding.daysRecycler.scrollToPosition(10)
    }

    override fun setListeners() {
        binding.ProfileLayout.setOnClickListener {
            findNavController().navigate(
                ScheduleFragmentDirections.actionScheduleFragmentToProfileFragment()
            )
        }
    }

    override fun setObservable() {

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

        viewModel.group.observe(viewLifecycleOwner) {
            binding.group.text = binding.root.context.resources.getString(R.string.schedule_group,it)
        }

        viewModel.lessonList.observe(viewLifecycleOwner) {
            lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
            dayAdapter.setOnClickListner {
                //TODO : ЛОГИКУ ПЕРЕНЕСТИ!
                val day = mainViewModel.schedule.value!!.getDayByDate(it.localDate.toLocalDate())
                if(day != null)
                    viewModel.viewLessonsOnDay(day)
                else
                    viewModel.clear()
            }
        }
    }
}