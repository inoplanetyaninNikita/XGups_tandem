package com.example.xgups_tandem.ui.grades

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentGradesBinding
import com.example.xgups_tandem.ui.grades.adapter.GradeAdapter
import java.lang.Math.abs


class GradesFragment : BaseFragment<FragmentGradesBinding>(FragmentGradesBinding::inflate) {
    val viewModel by viewModels<GradesViewModel>()
    val adapter = GradeAdapter()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.value = getProfileLogo()

        binding.names.text = "${mainViewModel.user.value?.secondName} ${mainViewModel.user.value?.firstName}"
        binding.group.text = mainViewModel.user.value?.bookNumber



        }

    override fun setAdapter() {
        binding.rv.adapter = adapter;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        binding.rv.setOnTouchListener(object : OnSwipeTouchListener(requireContext(), binding.rv){
            var lastY : Float = -1f
            var maxHeight = 100
            val view = binding.spinner

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                var y = event.y
                var action = event.action

                if (action == 0 ) lastY = y
                if (action == 2) {
                    var deltaY = y - lastY

                    val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
                    if((view.height + deltaY).toInt() > 0 ) {
                        if((view.height + deltaY).toInt() > maxHeight ){
                            layoutParams.height = maxHeight
                            view.setLayoutParams(layoutParams)
                        }
                        else{
                            layoutParams.height = (view.height + deltaY).toInt()
                            view.setLayoutParams(layoutParams)
                        }
                    }
                    if((view.height + deltaY).toInt() <= 0 ){
                        layoutParams.height = 0.toInt()
                        view.setLayoutParams(layoutParams);
                    }


                    Log.d("Position", "${deltaY.toString()} $action")
                }

                return super.onTouch(v, event)
            }
        })


    }

    override fun onStart() {
        super.onStart()

    }
    private fun increaseViewSize(view: View, isShow : Boolean, maxHeight : Int) {
        val valueAnimator = if(isShow) ValueAnimator.ofInt(view.measuredHeight, maxHeight)
        else ValueAnimator.ofInt(view.measuredHeight, 0)
        valueAnimator.duration = 50L
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams

            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        valueAnimator.start()
    }

    override fun setObservable() {
        adapter.submitList(mainViewModel.marks.value)

        spinnerConfigure()
        mainViewModel.marks.observe(viewLifecycleOwner){
            adapter.submitList(mainViewModel.marks.value)
            spinnerConfigure()
        }

        viewModel.image.observe(viewLifecycleOwner){
            binding.ivGrade.load(it) {
                crossfade(250)
                placeholder(R.mipmap.user)
                error(R.mipmap.user)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun spinnerConfigure() {
        if(mainViewModel.marks.value != null) {
            val values = arrayListOf<String>()
            for (item in mainViewModel.marks.value!!) {
                if(!values.contains(item.documentName)) {
                    values.add(item.documentName)
                }
            }
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, values)

            binding.spinner.adapter = spinnerAdapter

            binding.spinner?.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val list = mutableListOf<SamGUPS.MarkResponse>()
                for (item in mainViewModel.marks.value!!) {
                    if(item.documentName == values[position])
                        list.add(item)
                }
                adapter.submitList(list)
            }
        }
        }
    }



    open class OnSwipeTouchListener internal constructor(ctx:Context, mainView: View):View.OnTouchListener{
        private val gestureDetector: GestureDetector
        private var context: Context
        private lateinit var onSwipe:OnSwipeListener
        init{
            gestureDetector = GestureDetector(ctx, GestureListener())
            mainView.setOnTouchListener(this)
            context = ctx
        }
        override fun onTouch(v:View, event: MotionEvent):Boolean {
            return gestureDetector.onTouchEvent(event)
        }
        private companion object {
            private const val swipeThreshold = 100
            private const val swipeVelocityThreshold = 100
        }
        inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e:MotionEvent):Boolean {
                return true
            }
            override fun onFling(e1:MotionEvent, e2:MotionEvent, velocityX:Float, velocityY:Float):Boolean {
                var result = false
                try{
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (abs(diffX) > abs(diffY)){
                        if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold){
                            if (diffX > 0){
                                onSwipeRight()
                            }
                            else{
                                onSwipeLeft()
                            }
                            result = true
                        }
                    }
                    else if (abs(diffY) > swipeThreshold && abs(velocityY) > swipeVelocityThreshold){
                        if (diffY > 0){
                            onSwipeBottom()
                        }
                        else{
                            onSwipeTop()
                        }
                        result = true
                    }
                }
                catch (exception:Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        }
        internal fun onSwipeRight() {
        }
        internal fun onSwipeLeft() {
        }
        internal fun onSwipeTop() {
        }
        internal fun onSwipeBottom() {
        }
        internal interface OnSwipeListener {
            fun swipeRight()
            fun swipeTop()
            fun swipeBottom()
            fun swipeLeft()
        }
    }

}