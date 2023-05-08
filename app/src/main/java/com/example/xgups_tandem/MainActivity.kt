package com.example.xgups_tandem

import android.annotation.SuppressLint
import android.app.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.example.xgups_tandem.databinding.ActivityMainBinding
import com.example.xgups_tandem.di.PushNotification
import com.example.xgups_tandem.di.PushNotificationData
import com.example.xgups_tandem.room.AppDatabase
import com.example.xgups_tandem.room.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.containerMain) as NavHostFragment).navController
    }

    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    @Inject lateinit var push : PushNotification

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        push.show(PushNotificationData("Пара", "13:00"))

        //region start
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        val userDao = db.userDao()
        lifecycleScope.launch(Dispatchers.IO){
//             userDao.insertAll(User(0,"Toropovsky", "Nikita"))
        }
        lifecycleScope.launch(Dispatchers.IO){
            val users: List<User> = userDao.getAll()
            Log.d("fq",users.size.toString())
        }

        //endregion

        val context = binding.root.context

    }


}