package com.example.xgups_tandem.ui.grades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.xgups_tandem.MainViewModel
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentGradesBinding
import com.example.xgups_tandem.databinding.FragmentLoginBinding

class GradesFragment : BaseFragment<FragmentGradesBinding>(FragmentGradesBinding::inflate) {
    val viewModel by viewModels<GradesViewModel>()


}