package com.example.xgups_tandem.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentLoginBinding
import com.example.xgups_tandem.utils.ManagerUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

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
        viewModel.loginSuccessSamGUPS.observe(viewLifecycleOwner) {

        }

        viewModel.schedule.observe(viewLifecycleOwner)
        {
            val bundle = Bundle()
            bundle.putString("second_name", "Тороповский")
            bundle.putString("first_name", "Никита")

            mainViewModel.schedule.value = viewModel.schedule.value

            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToScheduleFragment("a", "b")
            )
        }
        viewModel.loginSuccessADFS.observe(viewLifecycleOwner) {
            if (it) {

                val bundle = Bundle()
                bundle.putString("second_name", "Тороповский")
                bundle.putString("first_name", "Никита")

                mainViewModel.schedule.value = viewModel.schedule.value

                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToScheduleFragment("a", "b")
                )
                //controller.navigate(R.id.scheduleFragment, bundle)
                //
            }
        }
    }
}