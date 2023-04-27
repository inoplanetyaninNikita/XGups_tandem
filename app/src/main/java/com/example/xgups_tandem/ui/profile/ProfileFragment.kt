package com.example.xgups_tandem.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentProfileBinding
import com.example.xgups_tandem.ui.login.LoginFragmentDirections

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.value = "https://sun7-13.userapi.com/impg/z2nfUVtnV_hFAaFEeN_oC0A7Iig22BRk1uXn_w/vKNF080jniU.jpg?size=1215x2160&quality=95&sign=cd8d8575e08036e92d9f79ce5a3b99cb&type=album"
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
//            findNavController().navigate(
//                ProfileFragmentDirections.actionProfileFragmentToGradesFragment()
//            )
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToHelpFragment()
            )

        }
    }

    override fun setObservable() {
        viewModel.image.observe(viewLifecycleOwner) {
            binding.imageProfile2.load(it) {
                crossfade(true)
                placeholder(R.mipmap.user)
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
}