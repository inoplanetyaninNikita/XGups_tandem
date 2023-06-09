package com.example.xgups_tandem.ui.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentScheduleBinding
import com.example.xgups_tandem.ui.schedule.dayadapter.DayAdapter
import com.example.xgups_tandem.ui.schedule.lessonAdapter.ScheduleLessonAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate)  {
    private val viewModel by viewModels<ScheduleViewModel>()
    private val dayAdapter = DayAdapter()
    private val lessonAdapter = ScheduleLessonAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Profile

        viewModel.image.value = getProfileLogo()
        viewModel.name.value = "${mainViewModel.user.value!!.secondName} ${mainViewModel.user.value!!.firstName}"
        viewModel.group.value = mainViewModel.user.value!!.group
        //endregion
        //region Отображение пар, на сегодня

        val today = mainViewModel.schedule.value!!.getDayByDate(LocalDate.now())
        today?.let {
            viewModel.viewLessonsOnDay(it)
        }
        //endregion

        binding.daysRecycler.scrollToPosition(viewModel.dateList.value!!.indexOf(viewModel.getToday()) - 2)

    }

    override fun setAdapter() {

        //region Day adapter

        dayAdapter.setListOnAdapter(viewModel.dateList.value!!)
        binding.daysRecycler.adapter = dayAdapter
        binding.daysRecycler.scrollToPosition(10)
        //endregion
        //region Lesson adapter

        lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
        binding.lessonsRecycler.adapter = lessonAdapter
        //endregion

    }

    override fun setListeners() {
        binding.ProfileLayout.setOnClickListener {
            findNavController().navigate(
                ScheduleFragmentDirections.actionScheduleFragmentToProfileFragment()
            )
        }
    }

    override fun setObservable() {

        //region Profie

        viewModel.name.observe(viewLifecycleOwner) {
            binding.names.text  = binding.root.context.resources.getString(R.string.schedule_names,it)
        }
        viewModel.image.observe(viewLifecycleOwner) {
            binding.imageProfile.load(it) {
                crossfade(250)
                placeholder(R.mipmap.user)
                error(R.mipmap.user)
                transformations(CircleCropTransformation())
            }
        }
        viewModel.group.observe(viewLifecycleOwner) {
            binding.group.text = binding.root.context.resources.getString(R.string.schedule_group,it)
        }
        //endregion
        //region Переключение дня и обновление адаптера с парами

        viewModel.lessonList.observe(viewLifecycleOwner) {
            lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
            dayAdapter.setOnClickListner {
                val day = mainViewModel.schedule.value!!.getDayByDate(it.localDate.toLocalDate())
                if (day != null) viewModel.viewLessonsOnDay(day) else viewModel.clear()
            }
        }
        //endregion

    }
}