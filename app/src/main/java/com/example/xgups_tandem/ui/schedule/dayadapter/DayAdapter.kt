package com.example.xgups_tandem.ui.schedule.dayadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.adapter.BaseListAdapter
import com.example.xgups_tandem.databinding.ItemDayBinding
import com.example.xgups_tandem.ui.schedule.ScheduleFragment
import java.time.LocalDateTime

/** Адаптер-календарь.
 * Для корректной работы нужно прикрепить [setListOnAdapter], а для обработки действий по нажатию на айтем слушаем [setOnClickListner].
 * @author Nikita Toropovsky
 * @sample ScheduleFragment.dayAdapterConfigure
 * @since 21.03.2023
 * */
class DayAdapter : RecyclerView.Adapter<DayAdapter.Holder>()  {
    /** Лист для отображения. */
    private lateinit var dateList : List<DayModel>
    /**Прикрепление коллекции.*/
    fun setListOnAdapter(list : List<DayModel>) {
        dateList = list
        dateList
        setDataListOnAdapter()
    }

    /**Коллекция для хранения данных View. */
    private lateinit var data : MutableList<HolderData>
    /**Создание пустой коллекции для данных View.*/
    private fun setDataListOnAdapter() {
        val today = LocalDateTime.now().dayOfYear
        data = emptyList<HolderData>().toMutableList()
        repeat(dateList.size)
        {
            if(dateList[it].localDate.dayOfYear == today) {
                data.add(HolderData(1,1))
            }
            else {
                data.add(HolderData(0,0))
            }

        }
    }

    /**Переменная для [setOnClickListner].*/
    private var actionOnClick: ((DayModel) -> Unit)? = null

    /** Событие при нажатии на айтем.*/
    fun setOnClickListner(action: ((DayModel) -> Unit)) {
        actionOnClick = action
    }

    /** Главный [Holder] в [DayAdapter]. */
    private var selectedHolder: Holder? = null

    /** Данные View для главного [Holder]*/
    private var selectedHolderData: HolderData? = null

    /** Переключение главного [Holder]. */
    private var selectItem: ((Holder, HolderData) -> Unit) = { it, itData->
        selectedHolderData?.typeNow   = selectedHolderData?.type!!
        selectedHolder?.unSelect()

        selectedHolder = it
        selectedHolderData = itData

        itData.typeNow = 2
        it.onSelect()
    }

    /** Класс [Holder], нужен для отображения дня. */
    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemDayBinding.bind(item)
        private val context = binding.root.context

        private lateinit var data : HolderData
        /**
         * @param date данные о дате
         * @param data данные о View
         * @param onClick вызываем событие при нажатии на день
         * @param selectItem вызываем событие при нажатии на день, чтобы выделить элемент*/
        fun bind(
            date: DayModel, //данные о дате
            data: HolderData, //данные view
            onClick:((DayModel)-> Unit)?, //событие для viewmodel при клике
            selectItem: (Holder, HolderData) -> Unit //событие для адаптера для изменения выделенного итема
        ) = with(binding)
        {

            when(date.status) {
                DayStatus.HOLIDAY -> data.type = HOLIDAY
                DayStatus.NOHOLIDAY -> data.type = NOHOLIDAY
                DayStatus.TODAY -> data.type = TODAY
                else -> {}
            }
            data.typeNow = data.type

            if (data.typeNow == SELECT) selectItem.invoke(this@Holder, data)
            this@Holder.data = data

            val num = date.localDate.dayOfMonth.toString()
            val name = context.resources.getStringArray(R.array.short_days)[date.localDate.dayOfWeek.value]

            //Привязываем слушателя к кнопке, чтобы словить cобытие в ViewModel
            binding.root.setOnClickListener{
                selectItem.invoke(this@Holder, data)
                onClick?.invoke(date)
            }

            dayName.text = name
            dayNumber.text = num



            updateColor()
        }

        /**
         * Константы для изменения типа дня
         * @param HOLIDAY  выходной
         * @param TODAY  сегодня
         * @param SELECT  выбранный день
         * @param NOHOLIDAY  есть пары
         * */
        private companion object {
            const val HOLIDAY = 0
            const val TODAY = 1
            const val SELECT = 2
            const val NOHOLIDAY = 4
        }

        /** Выделение элемента. *(Меняются цвета)* */
        fun onSelect()
        {
            updateColor()
        }

        /** Снятие выделения. *(Меняются цвета)* */
        fun unSelect()
        {
            updateColor()
        }

        /** Обновляем цвета после события. */
        private fun updateColor()
        {
            when(data.typeNow)
            {
                HOLIDAY -> changeColors(R.color.xgtransperent,R.color.xggraywhite, R.color.xggraywhite)
                TODAY -> changeColors(R.color.xggray, R.color.black, R.color.black)
                SELECT -> changeColors(R.color.xgpurple, R.color.xgpurple, R.color.xgpurple)
                NOHOLIDAY -> changeColors(R.color.xggray, R.color.xggray, R.color.xggray)
            }
        }

        /**
         * Изменение цветов [Holder].
         * @param resourceBackgroudColor цвет бэкграунда
         * @param resourceNumTextColor цвет текста даты
         * @param resourceNameTextColor цвет текста названия дня недели
         */
        private fun changeColors(resourceBackgroudColor : Int = 0, resourceNumTextColor : Int = 0, resourceNameTextColor : Int = 0) {
            if (resourceBackgroudColor != 0) binding.dayIsSelect.setCardBackgroundColor(context.resources.getColor(resourceBackgroudColor))
//            if (resourceBackgroudColor != 0) binding.root.setCardBackgroundColor(context.resources.getColor(resourceBackgroudColor))
            if (resourceNumTextColor != 0) binding.dayNumber.setTextColor(context.resources.getColor(resourceNumTextColor))
            if (resourceNameTextColor != 0) binding.dayName.setTextColor(context.resources.getColor(resourceNameTextColor))
        }
    }

    /** [HolderData] нужен, для того чтоб нормально хранить данные View для определенного элемента [Holder].
     * *Так как [RecyclerView] не создает новые элементы, а перезаписывает их, получается так, что при скроле [Holder] отображает View другого элемента*. */
    data class HolderData(var type: Int = 0, var typeNow : Int = 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(
            dateList[position],
            data[position],
            actionOnClick,
            selectItem)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
    fun NotifyChange()
    {
        notifyDataSetChanged()
    }
}