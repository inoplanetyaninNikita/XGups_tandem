package com.example.xgups_tandem.ui.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners()  {
        binding.btnLogin.setOnClickListener {

            //(activity as MainActivity).show()
            viewModel.login(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.email.setOnFocusChangeListener { _, it ->
            if (it)
                binding.email.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
            else {
                if (viewModel.validateEmail(binding.email.text.toString()))
                    binding.email.background =
                        ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
                else
                    binding.email.background =
                        ContextCompat.getDrawable(binding.root.context, R.drawable.border_fail)
            }
        }
    }

    override fun setObservable() {

        viewModel.schedule.observe(viewLifecycleOwner) {
            mainViewModel.schedule.value = viewModel.schedule.value
            mainViewModel.user.value = viewModel.dataUser.value

            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToScheduleFragment(viewModel.dataUser.value!!.secondName,
                    viewModel.dataUser.value!!.firstName)
            )
        }
    }

    override fun onBackPressed() {
    }
}