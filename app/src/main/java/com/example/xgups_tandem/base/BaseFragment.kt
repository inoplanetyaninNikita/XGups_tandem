package com.example.xgups_tandem.base

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.example.xgups_tandem.MainViewModel
import java.io.File
import kotlin.io.path.Path

abstract class BaseFragment<VB : ViewBinding>(
    private val vbInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    protected open val isBnvVisible = true

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected val mainViewModel by activityViewModels<MainViewModel>()

    protected open val navigationController by lazy { findNavController() }

    protected val windowInsetsController by lazy {
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        )
    }

    protected open fun onCreateView() {}
    protected open fun initToolbar() {}
    protected open fun setListeners() {}
    protected open fun setObservable() {}
    protected open fun setAdapter() {}

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transition =
            TransitionInflater
                .from(requireContext())
                .inflateTransition(android.R.transition.slide_right)
                .setDuration(250L)

        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        _binding = vbInflate(inflater, container, false)
        return binding.root
    }

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

    // МОЕ //

    protected fun getProfileLogo() : String{
        return Path(requireContext().externalCacheDir!!.path, "profile.jpeg").toString()
    }
    protected fun getProfileLogo(login: String) : String{
        return Path(requireContext().externalCacheDir!!.path, "${login}.jpeg").toString()
    }
}