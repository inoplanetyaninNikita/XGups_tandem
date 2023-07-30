package com.example.xgups_tandem.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.xgups_tandem.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(
    private val vbInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected val windowInsetsController by lazy {
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        )
    }

    protected val navigationController by lazy { findNavController() }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = vbInflate(inflater, container, false)
        onCreateView()
//        Зачем то включается клава :/
//        windowInsetsController.show(WindowInsetsCompat.Type.ime())
        return binding.root
    }

    protected open fun showKeyboard() {
        windowInsetsController.show(WindowInsetsCompat.Type.ime())
    }

    protected open fun hideKeyboard() {
        windowInsetsController.hide(WindowInsetsCompat.Type.ime())
    }

    @Deprecated("use onViewCreated")
    protected open fun onCreateView() {}

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservable()
        setAdapter()
        initOnBackPressedDispatcher()
    }

    protected open fun initOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    protected open fun onBackPressed() {
        navigationController.popBackStack()
    }

    protected open fun setObservable() {}
    protected open fun setListeners() {}
    protected open fun setAdapter() {}

    @CallSuper
    protected open fun setSettingsDialog(dialog: BottomSheetDialog) {
        with(dialog) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.BottomSheetDialogAnimation
        dialog.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        dialog.setOnShowListener {
            setSettingsDialog((it as BottomSheetDialog))
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}