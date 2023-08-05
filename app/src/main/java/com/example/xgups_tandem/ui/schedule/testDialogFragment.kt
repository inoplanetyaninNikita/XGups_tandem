package com.example.xgups_tandem.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.example.xgups_tandem.MainViewModel
import com.example.xgups_tandem.R
import com.example.xgups_tandem.databinding.FragmentTestDialogBinding
import java.lang.reflect.Type

class testDialogFragment : BaseDialogFragment<FragmentTestDialogBinding>(FragmentTestDialogBinding::inflate) {

}

abstract class BaseDialogFragment<VB : ViewBinding>(
    private val vbInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : DialogFragment()
{

    protected val mainViewModel by activityViewModels<MainViewModel>()
    protected open val navigationController by lazy { findNavController() }

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = vbInflate(inflater, container, false)
        return binding.root
    }

    protected open fun onCreateView() {}
    protected open fun initToolbar() {}
    protected open fun setListeners() {}
    protected open fun setObservable() {}
    protected open fun setAdapter() {}

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initOnBackPressedDispatcher()
        setListeners()
        setObservable()
        setAdapter()
    }

    protected open fun initOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    protected open fun onBackPressed() {
        navigationController.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
