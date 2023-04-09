package com.example.xgups_tandem.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentLoginBinding
import com.example.xgups_tandem.ui.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log


@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()

    override fun setListeners()  {
        loginButton()
        val tw = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int,
                                   before: Int, count: Int) {
            loginButton()
        } }

        viewModel.mainViewModel = mainViewModel

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.email.setOnFocusChangeListener { _, it ->
//            if (it)
//                binding.email.background =
//                    ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
//            else {
//                if (viewModel.validateEmail(binding.email.text.toString()))
//                    binding.email.background =
//                        ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
//                else
//                    binding.email.background =
//                        ContextCompat.getDrawable(binding.root.context, R.drawable.border_fail)
//            }
        }

        binding.email.addTextChangedListener(tw)
        binding.password.addTextChangedListener(tw)
    }

    override fun setObservable() {

        viewModel.loginSuccessSamGUPS.observe(viewLifecycleOwner) {
            if(!it) incorrectLoginAlert()
        }

        viewModel.schedule.observe(viewLifecycleOwner) {
            mainViewModel.schedule.value = viewModel.schedule.value
            mainViewModel.user.value = viewModel.dataUser.value

            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToScheduleFragment(viewModel.dataUser.value!!.secondName,
                    viewModel.dataUser.value!!.firstName)
            )

        }

        viewModel.marks.observe(viewLifecycleOwnerLiveData.value!!){
            mainViewModel.marks.value = it;
        }
    }

    override fun onBackPressed() {
    }

    private fun loginButton() {
        val login = viewModel.canPressToButton(binding.email.text.toString(),
            binding.password.text.toString())
        binding.btnLogin.isEnabled = login
        if (login)
            binding.btnLogin.backgroundTintList =  ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.xgpurple))
        else
            binding.btnLogin.backgroundTintList =  ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.xggray))

    }

    private fun incorrectLoginAlert() {
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle("Аккаунт")
        alertDialogBuilder.setMessage("Неверный логин или пароль")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Ок") { dialog, id ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}