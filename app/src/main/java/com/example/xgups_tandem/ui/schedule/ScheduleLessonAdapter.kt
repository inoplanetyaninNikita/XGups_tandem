package com.example.xgups_tandem.ui.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xgups_tandem.R
import com.example.xgups_tandem.databinding.ItemLessonBinding
import com.example.xgups_tandem.ui.schedule.dayadapter.DayModel

class ScheduleLessonAdapter : RecyclerView.Adapter<ScheduleLessonAdapter.Holder>() {

    lateinit var lessonList : List<ScheduleViewModel.Lesson>
    private var actionOnClick: ((ScheduleViewModel.Lesson) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListOnAdapter(list : List<ScheduleViewModel.Lesson>)
    {
        lessonList = list
        notifyDataSetChanged()
    }

    /**Прикрепление коллекции.*/
    class Holder(item: View) : RecyclerView.ViewHolder(item) {

        private val binding = ItemLessonBinding.bind(item)
        fun bind(lesson: ScheduleViewModel.Lesson,
                 onClick:((ScheduleViewModel.Lesson)-> Unit)? ) = with(binding)
        {
            val context = binding.root.context

            //Setters
            val name = String.format(
                context.resources.getString(R.string.lesson_name),
                lesson.lessonName)
            val teacher = String.format(
                context.resources.getString(R.string.lesson_teacher),
                lesson.teacher)
            val auditorium = String.format(
                context.resources.getString(R.string.lesson_auditorium),
                lesson.auditorium)
            val startTime = String.format(
                context.resources.getString(R.string.lesson_startTime),
                lesson.startTime.hour,
                lesson.startTime.minute)
            val endTime = String.format(
                context.resources.getString(R.string.lesson_endTime),
                lesson.endTime.hour,
                lesson.endTime.minute)

            binding.nameLesson.text = name
            binding.teacherLesson.text = teacher
            binding.auditoriumLesson.text = auditorium
            binding.startTime.text = startTime
            binding.endTime.text = endTime

            binding.root.setOnClickListener{
                onClick?.invoke(lesson)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(lessonList[position], actionOnClick)
    }

    fun setOnClickListner(action: ((ScheduleViewModel.Lesson) -> Unit)) {
        actionOnClick = action
    }

}