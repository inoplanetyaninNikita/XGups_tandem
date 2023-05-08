package com.example.xgups_tandem

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import com.example.xgups_tandem.base.OnSwipeTouchListener
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentTesterBinding
import com.example.xgups_tandem.databinding.ItemDayBinding
import com.example.xgups_tandem.databinding.ItemLessonBinding
import com.google.android.material.progressindicator.CircularProgressIndicator


class tester : BaseFragment<FragmentTesterBinding>(FragmentTesterBinding::inflate)  {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        binding.frameTest.setOnTouchListener(object : OnSwipeTouchListener(requireContext(), binding.frameTest){
            var startX = 0f
            var startY = 0f
            var startTranslateX = 0f
            var startTranslateY = 0f
            var lastTime = 0L

            @SuppressLint("SetTextI18n")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val x = event.x
                val y = event.y

                var deltaX = x - startX
                var deltaY = y - startY

                val action = event.action

                if (action == 0 ) {
                    startX  = x
                    startY = y
                    startTranslateX = binding.frameTest.translationX
                    startTranslateY = binding.frameTest.translationY

                    lastTime = event.eventTime
                }
                if (action == 2) {
                    binding.frameTest.translationX = binding.frameTest.translationX + deltaX
                    binding.frameTest.translationY = binding.frameTest.translationY + deltaY
                }
                if (action == 1 &&
                    event.eventTime - lastTime > 1 &&
                    Math.abs(binding.frameTest.translationX - startTranslateX) +
                    Math.abs(binding.frameTest.translationY - startTranslateY) < 100 )
                {
                    lastTime = event.eventTime - lastTime
                    val btn = Button(context)
                    btn.apply {

                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )
                        translationX = Math.abs(x) - 100 //Math.abs(binding.frameTest.translationX) + 2000
                        translationY = Math.abs(y) - 100 //Math.abs(binding.frameTest.translationY) + 2000

                        text = "gogogo"
                    }

                    var progressBar = CircularProgressIndicator(context!!)
                    progressBar.apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        translationX = Math.abs(x) - 100 //Math.abs(binding.frameTest.translationX) + 2000
                        translationY = Math.abs(y) - 100

                        trackColor = Color.BLACK


                        progress = 15
                    }

                    binding.frameTest.addView(progressBar)

//                    Log.d("test","${binding.frameTest.translationX - startTranslateX} ${binding.frameTest.translationY - startTranslateY}")
                }

                if(action == 261){
                    Log.d("tag", "${event.x} ${event.y}")
                }

                Log.d("test", "${event.x} ${event.y}")
                return super.onTouch(v, event)
            }

        })
    }
}