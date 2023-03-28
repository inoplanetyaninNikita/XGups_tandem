package com.example.xgups_tandem.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.xgups_tandem.MainActivity
import com.example.xgups_tandem.R
import com.example.xgups_tandem.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListners()
    }

    fun setListners()
    {
        val controller = findNavController()

        binding.btnLogin.setOnClickListener {
            (activity as MainActivity).show()
            viewModel.login(binding.email.text.toString(),
                            binding.password.text.toString())
        }

        binding.email.setOnFocusChangeListener { _, it ->

            if(it)
                binding.email.background = ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
            else
            {
                if(viewModel.validateEmail(binding.email.text.toString()))
                    binding.email.background = ContextCompat.getDrawable(binding.root.context, R.drawable.border_standart)
                else
                    binding.email.background = ContextCompat.getDrawable(binding.root.context, R.drawable.border_fail)
            }
        }

        viewModel.loginSuccessADFS.observe(viewLifecycleOwner) {
            if(it)
            {
                val bundle = Bundle()

                bundle.putString("second_name", viewModel.secondName.value)
                bundle.putString("first_name", viewModel.firstName.value)
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToScheduleFragment("a","b")
                )
                //controller.navigate(R.id.scheduleFragment, bundle)
                //
            }
        }

        viewModel.loginSuccessSamGUPS.observe(viewLifecycleOwner) {
        }

    }


}