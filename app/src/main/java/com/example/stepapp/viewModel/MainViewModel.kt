package com.example.stepapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stepapp.model.Step


class MainViewModel() : ViewModel() {

    private val _steps = MutableLiveData(Step());
    private val _progress = MutableLiveData(0);

    val steps: LiveData<Step>
        get() = _steps;

    val progress : LiveData<Int>
        get() = _progress

    fun setStep(step : Int){
        val newStep = _steps.value
        newStep?.count = step
        _steps.postValue(newStep)
    }
}