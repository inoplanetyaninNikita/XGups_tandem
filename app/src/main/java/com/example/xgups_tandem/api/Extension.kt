package com.example.xgups_tandem.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

//region JSON

/** Конвертировать из JSON строки в любой [T]-класс. */
inline fun <reified T> String.convertJsonToClass() : T
{
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java).lenient()
    return  jsonAdapter.fromJson(this)!!
}

/** Конвертировать из [Any]-класса в строчку JSON. */
fun Any.convertClassToJson(): String
{
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
