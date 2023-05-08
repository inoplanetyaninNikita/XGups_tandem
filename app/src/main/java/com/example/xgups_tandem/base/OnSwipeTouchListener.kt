package com.example.xgups_tandem.base

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper

open class OnSwipeTouchListener internal constructor(ctx: Context, mainView: View): View.OnTouchListener{
    private val gestureDetector: GestureDetector
    private var context: Context

    private companion object {
        private const val swipeThreshold = 100
        private const val swipeVelocityThreshold = 100
    }

    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent):Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX:Float, velocityY:Float):Boolean {
            var result = false
            try{
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)){
                    if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold){
                        result = true
                    }
                }
                else if (Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold){
                    result = true
                }
            }
            catch (exception:Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    init{
        gestureDetector = GestureDetector(ctx, GestureListener())
        mainView.setOnTouchListener(this)
        context = ctx
    }

    open override fun onTouch(v: View, event: MotionEvent):Boolean {
        return gestureDetector.onTouchEvent(event)
        return true
    }




}
