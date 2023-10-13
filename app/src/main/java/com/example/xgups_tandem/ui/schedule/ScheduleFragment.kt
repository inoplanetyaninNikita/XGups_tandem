package com.example.xgups_tandem.ui.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentScheduleBinding
import com.example.xgups_tandem.ui.schedule.dayadapter.DayAdapter
import com.example.xgups_tandem.ui.schedule.dayadapter.DayStatus
import com.example.xgups_tandem.ui.schedule.lessonAdapter.ScheduleLessonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private val viewModel by viewModels<ScheduleViewModel>()
    private val dayAdapter = DayAdapter()
    private val lessonAdapter = ScheduleLessonAdapter()

    private val contents: MutableLiveData<List<Moodle.Content>> by lazy {
        MutableLiveData<List<Moodle.Content>>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeColorsForDays()

        //region Profile

        viewModel.image.value = getProfileLogo()
        viewModel.name.value =
            "${mainViewModel.user.value!!.secondName} ${mainViewModel.user.value!!.firstName}"
        viewModel.group.value = mainViewModel.user.value!!.group
        //endregion
        //region Отображение пар, на сегодня

        val today = mainViewModel.schedule.value!!.getDayByDate(LocalDate.now())
        today?.let {
            viewModel.viewLessonsOnDay(it)
        }
        //endregion

        binding.daysRecycler.scrollToPosition(viewModel.dateList.value!!.indexOf(viewModel.getToday()) - 2)
        viewLovePicture()
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
//        binding.ProfileLayout.setOnClickListener {
//            findNavController().navigate(
//                ScheduleFragmentDirections.actionScheduleFragmentToProfileFragment()
//            )
//        }

    }

    override fun setObservable() {

        //region Profie

        viewModel.name.observe(viewLifecycleOwner) {
//            binding.names.text = "${it.split(' ')[1]} "
        }
        viewModel.image.observe(viewLifecycleOwner) {
//            binding.imageProfile.load(it) {
//                crossfade(250)
//                placeholder(R.mipmap.user)
//                error(R.mipmap.user)
//                transformations(CircleCropTransformation())
//            }
        }
        viewModel.group.observe(viewLifecycleOwner) {
//            binding.group.text = binding.root.context.resources.getString(R.string.schedule_group,it)

        }


        //endregion
        //region Переключение дня и обновление адаптера с парами
        viewModel.lessonList.observe(viewLifecycleOwner) {
            lessonAdapter.setListOnAdapter(viewModel.lessonList.value!!)
            dayAdapter.setOnClickListner {
                var day = mainViewModel.schedule.value!!.getDayByDate(it.localDate.toLocalDate())
                if (day != null) viewModel.viewLessonsOnDay(day) else viewModel.clear()
                viewLovePicture()
            }


        }

        lessonAdapter.setOnClickListner {
            lifecycleScope.launch {
                try
                {
                    if (Moodle.token.isEmpty() ||
                        Moodle.userID == -1
                    ) return@launch

                    val courses = getCoursesForLesson(it.
                    lessonName.replace("пр.", "")
                        .replace("лаб.", "")
                        .replace("л.", ""))
                    contents.value = getAllElementsForCourse(courses[0])
                }
                catch(ex :Exception)
                {

                }

            }
        }
        contents.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                return@observe

//            val frag = testDialogFragment ()
//            val ft = requireFragmentManager().beginTransaction()
//            frag.show(ft, "custom_dialog")

            try {
                findNavController().navigate(
                    ScheduleFragmentDirections.actionScheduleFragmentToBooksFragment(
                        books = contents.value.orEmpty().toTypedArray()
                    )
                )
            } catch (_: Exception) {

            }
        }

//        binding.tvDrop.dropDownList(binding.llDrop)

        //endregion
    }



    private fun viewLovePicture(){
        if(viewModel.lessonList.value?.isEmpty() == true) {
            binding.ivLessonEmpty.visibility = View.VISIBLE
            binding.tvLessonEmpty.visibility = View.VISIBLE
        }
        else{
            binding.ivLessonEmpty.visibility = View.INVISIBLE
            binding.tvLessonEmpty.visibility = View.INVISIBLE
        }
    }

    private fun changeColorsForDays(){
        val days = viewModel.dateList.value!!
        for (day in days){
            val dayModel = mainViewModel.schedule.value!!.getDayByDate(day.localDate.toLocalDate())
            if (dayModel != null && dayModel.lessons.isNotEmpty()) {
                day.status = DayStatus.NOHOLIDAY
            }
            else{
                day.status = DayStatus.HOLIDAY
            }
        }
    }
    private fun getCoursesForLesson(lessonName : String) : List<Moodle.Course> {
        return mainViewModel.courses.value!!.filter { course ->
            course.fullname
                .toLowerCase(Locale.ROOT)
                .replace("\"","")
                .replace(" ","")

                .contains(lessonName
                    .replace("\"","")
                    .replace(" ",""),
                    ignoreCase = true)
        }
    }
    private suspend fun getAllElementsForCourse(course: Moodle.Course) : List<Moodle.Content>{
        val contents = mutableListOf<Moodle.Content>()
        val courseElement = Moodle.API.getDataOnCourse(courseid = course.id).body()
        for(chapter in courseElement.orEmpty()) {
            for (module in chapter.modules) {
                if(module.contents != null) {
                    for (content in module.contents) {
                        if (content.mimetype == "application/pdf") {
                            contents.add(content)
                        }
                    }
                }

            }
        }
        return contents
    }
}