package com.example.xgups_tandem.ui.schedule.bottomSheetDialog

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xgups_tandem.utils.ManagerUtils
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    val fileToOpen : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun downloadFile(url: String, destination: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw Exception("Ошибка при скачивании файла: ${response.code}")
                    }

                    val inputStream = response.body?.byteStream()
                    val outputFile = File(destination)

                    FileOutputStream(outputFile).use { outputStream ->
                        val data = ByteArray(1024)
                        var bytesRead: Int
                        while (inputStream?.read(data).also { bytesRead = it ?: -1 } != -1) {
                            outputStream.write(data, 0, bytesRead)
                        }
                    }

                    viewModelScope.launch(Dispatchers.Main) {
                        fileToOpen.value = destination
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибок
                Log.e("DownloadFile", "Ошибка при скачивании файла: ${e.message}", e)
            }
        }
    }
}

