package com.example.xgups_tandem.ui.grades

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.xgups_tandem.R
import com.example.xgups_tandem.base.OnSwipeTouchListener
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.base.BaseFragment
import com.example.xgups_tandem.databinding.FragmentGradesBinding
import com.example.xgups_tandem.ui.grades.adapter.GradeAdapter


class GradesFragment : BaseFragment<FragmentGradesBinding>(FragmentGradesBinding::inflate) {
    val viewModel by viewModels<GradesViewModel>()
    val adapter = GradeAdapter()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.value = getProfileLogo()

        binding.names.text = "${mainViewModel.user.value?.secondName} ${mainViewModel.user.value?.firstName}"
        binding.group.text = mainViewModel.user.value?.bookNumber



        }

    override fun setAdapter() {
        binding.rv.adapter = adapter;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        binding.rv.setOnTouchListener(object : OnSwipeTouchListener(requireContext(), binding.rv){
            var lastY : Float = -1f
            var maxHeight = 100
            val view = binding.spinner

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                var y = event.y
                var action = event.action

                if (action == 0 ) lastY = y
                if (action == 2) {
                    var deltaY = y - lastY

                    val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
                    if((view.height + deltaY).toInt() > 0 ) {
                        if((view.height + deltaY).toInt() > maxHeight ){
                            layoutParams.height = maxHeight
                            view.setLayoutParams(layoutParams)
                        }
                        else{
                            layoutParams.height = (view.height + deltaY).toInt()
                            view.setLayoutParams(layoutParams)
                        }
                    }
                    if((view.height + deltaY).toInt() <= 0 ){
                        layoutParams.height = 0.toInt()
                        view.setLayoutParams(layoutParams);
                    }


                    Log.d("Position", "${deltaY.toString()} $action")
                }

                return super.onTouch(v, event)
            }
        })


    }

    override fun onStart() {
        super.onStart()

    }


    override fun setObservable() {
        adapter.submitList(mainViewModel.marks.value)

        spinnerConfigure()
        mainViewModel.marks.observe(viewLifecycleOwner){
            adapter.submitList(mainViewModel.marks.value)
            spinnerConfigure()
        }

        viewModel.image.observe(viewLifecycleOwner){
            binding.ivGrade.load(it) {
                crossfade(250)
                placeholder(R.mipmap.user)
                error(R.mipmap.user)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun spinnerConfigure() {
        if(mainViewModel.marks.value != null) {
            val values = arrayListOf<String>()
            for (item in mainViewModel.marks.value!!) {
                if(!values.contains(item.documentName)) {
                    values.add(item.documentName)
                }
            }
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, values)

            binding.spinner.adapter = spinnerAdapter

            binding.spinner?.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val list = mutableListOf<SamGUPS.MarkResponse>()
                for (item in mainViewModel.marks.value!!) {
                    if(item.documentName == values[position])
                        list.add(item)
                }
                adapter.submitList(list)
            }
        }
        }
    }




}