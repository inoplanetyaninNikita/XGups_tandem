package com.example.xgups_tandem.api

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.forEach
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Type

//region JSON

/** Конвертировать из JSON строки в любой [T]-класс. */
inline fun <reified T> String.convertJsonToClass() : T {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java).lenient()
    return  jsonAdapter.fromJson(this)!!
}


inline fun <reified T> String.convertJsonToClassGSON(): T {
    return Gson().fromJson(this, object : TypeToken<T>() {}.type)
}


/** Конвертировать из [Any]-класса в строчку JSON. */
fun Any.convertClassToJson(): String {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<Any> = moshi.adapter(Any::class.java)
    return jsonAdapter.toJson(this)
}

//endregion
//region Bitmap

    //region Blur
    fun Bitmap.blurRenderScript(context: Context?, radius: Int): Bitmap? {
    var smallBitmap = this
    try {
        smallBitmap = RGB565toARGB888(this)!!
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val bitmap = Bitmap.createBitmap(
        smallBitmap!!.width, smallBitmap.height,
        Bitmap.Config.ARGB_8888
    )
    val renderScript = RenderScript.create(context)
    val blurInput = Allocation.createFromBitmap(renderScript, smallBitmap)
    val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)
    val blur = ScriptIntrinsicBlur.create(
        renderScript,
        Element.U8_4(renderScript)
    )
    blur.setInput(blurInput)
    blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
    blur.forEach(blurOutput)
    blurOutput.copyTo(bitmap)
    renderScript.destroy()
    return bitmap
}
    private fun RGB565toARGB888(img: Bitmap): Bitmap? {
    val numPixels = img.width * img.height
    val pixels = IntArray(numPixels)

    //Get JPEG pixels.  Each int is the color values for one pixel.
    img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)

    //Create a Bitmap of the appropriate format.
    val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)

    //Set RGB pixels.
    result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
    return result
}

    //endregion

fun saveImage(bitmap: Bitmap, path: String, updateImage: MutableLiveData<String>? = null){
    val f = File(path)
    f.createNewFile()

    val os: OutputStream = BufferedOutputStream(FileOutputStream(f))
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
    os.close()

    updateImage?.value = path
}

fun loadImage(path: String) : Bitmap{
    return BitmapFactory.decodeFile(path)
}


//endregion
//region VIEW

fun View.dropDownList(viewGroup: ViewGroup){
    this.setOnClickListener{
        viewGroup.forEach {
//            it.visibility = if (it.visibility == View.GONE)
//                View.VISIBLE else View.GONE
        }
            val it = viewGroup
            val isOpen = it.visibility == View.VISIBLE
            val startHeight: Int
            val endHeight: Int

            if (isOpen) {
                startHeight = it.height
                endHeight = 0
            } else {
                startHeight = 0
                it.measure(
                    View.MeasureSpec.makeMeasureSpec(it.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                endHeight = it.measuredHeight
            }

            val animator = ValueAnimator.ofInt(startHeight, endHeight)
            animator.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = it.layoutParams
                layoutParams.height = value
                it.layoutParams = layoutParams
            }

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    it.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (isOpen) {
                        it.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            animator.duration = 300
            animator.start()

    }
}
//endregion
