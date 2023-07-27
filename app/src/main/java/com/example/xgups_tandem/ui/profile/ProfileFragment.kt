package com.example.xgups_tandem.ui.profile

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.loadImage
import com.example.xgups_tandem.api.saveImage
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentProfileBinding
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel by viewModels<ProfileViewModel>()

    //region Базовый фрагмент

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("test", getProfileLogo())
        viewModel.image.value = getProfileLogo()
        viewModel.name.value = "${mainViewModel.user.value!!.secondName} ${mainViewModel.user.value!!.firstName} ${mainViewModel.user.value!!.thirdName}"
        viewModel.group.value = mainViewModel.user.value!!.bookNumber
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        binding.backArrowProfile.setOnClickListener{
            onBackPressed()
        }
        binding.logout.root.setOnClickListener{
            logoutAlert()
        }
        binding.grade.root.setOnClickListener{
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToGradesFragment()
            )

//            findNavController().navigate(
//                ProfileFragmentDirections.actionProfileFragmentToTester()
//            )
        }
        binding.chatbot.root.setOnClickListener{
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToHelpFragment()
            )
        }

        binding.addPhoto.setOnClickListener{
            openCameraIntent()
        }
    }
    override fun setObservable() {
        viewModel.image.observe(viewLifecycleOwner) {
            binding.imageProfile2.load(it) {
                crossfade(250)
                placeholder(R.mipmap.user)
                error(R.mipmap.user)
                transformations(CircleCropTransformation())
            }
        }
        viewModel.name.observe(viewLifecycleOwner){
            binding.names2.text  = viewModel.name.value
        }
        viewModel.group.observe(viewLifecycleOwner){
            binding.group2.text  = viewModel.group.value
        }
    }

    //endregion
    //region Функции меню

    private fun logoutAlert() {
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle("Аккаунт")
        alertDialogBuilder.setMessage("Вы уверены, что хотите выйти")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Да") { dialog, id ->
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            )
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Нет"){ dialog, id ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    //endregion
    //region Работа с Intent'а ми

    companion object {
        val ACTIVITY_RESULT_CLOSE = 0
        val ACTIVITY_PHOTO_GALLERY = 1
        val ACTIVITY_PHOTO_CAMERA = 2
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == ACTIVITY_RESULT_CLOSE) return

        if(requestCode == ACTIVITY_PHOTO_GALLERY) {
            val bitmap = loadImage(data?.data.toString())
            saveImage(bitmap,
                getProfileLogo(),
                viewModel.image)

        }
        if(requestCode == ACTIVITY_PHOTO_CAMERA){
            saveImage(data?.extras?.get("data") as Bitmap,
                getProfileLogo(),
                viewModel.image)
        }
    }
    private fun openGalleryIntent(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        activity?.parent?.startActivityForResult(intent, ACTIVITY_PHOTO_GALLERY)
    }
    private fun openCameraIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, ACTIVITY_PHOTO_CAMERA)
        } catch (_: ActivityNotFoundException) { }
    }
    //endregion

}