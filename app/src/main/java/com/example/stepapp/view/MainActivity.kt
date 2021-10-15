package com.example.stepapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.stepapp.R
import com.example.stepapp.databinding.ActivityMainBinding
import com.example.stepapp.viewModel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import java.util.concurrent.TimeUnit

const val TAG = "MAIN_ACTIVITY_LOGGING";

class MainActivity : AppCompatActivity() {

    private val GOOGLE_FIT_REQUEST_CODE = 1001
    private val PERRMISSION_ACTIVITY_RECOGNITION_REQ_CODE = 1002

    private val fitnessOptions: FitnessOptions by lazy {
        FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    private val account: GoogleSignInAccount by lazy {
        getGoogleAccount()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val listener =
        OnDataPointListener { dataPoint ->
            for (field in dataPoint.dataType.fields) {
                val value = dataPoint.getValue(field)

                Log.i(TAG, "Detected DataPoint field: ${field.name}")
                Log.i(TAG, "Detected DataPoint value: $value")

                viewModel.setStep(value.asInt())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUI()
        checkPermission()
        initGoogleSignIn()
    }


    override fun onStart() {
        super.onStart()
        createListener()
    }

    override fun onStop() {
        super.onStop()
        removeListener()
    }

    override fun onPause() {
        super.onPause()
        removeObservers()
    }

    override fun onResume() {
        super.onResume()
        createObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERRMISSION_ACTIVITY_RECOGNITION_REQ_CODE -> {
                initGoogleSignIn()
            }
            GOOGLE_FIT_REQUEST_CODE -> {
                readSavedSteps()
                createListener()
            }
        }
    }

    private fun createObservers(){
        viewModel.steps
    }

    private fun removeObservers(){
        viewModel.steps.removeObservers(this)
    }

    private fun setUI(){
        val mainActivityBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainActivityBinding.vm = viewModel
        mainActivityBinding.lifecycleOwner = this
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.ACCESS_FINE_LOCATION),
                PERRMISSION_ACTIVITY_RECOGNITION_REQ_CODE
            )
        }
    }

    private fun initGoogleSignIn(){
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(this, GOOGLE_FIT_REQUEST_CODE, account, fitnessOptions)
        } else {
            readSavedSteps()
        }
    }

    private fun createListener(){
        Fitness.getSensorsClient(this, account)
            .add(
                SensorRequest.Builder()
                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                    .setSamplingRate(3, TimeUnit.SECONDS)
                    .build(),
                listener
            )
            .addOnSuccessListener {
                Log.i(TAG, "Listener registered!")
            }
            .addOnFailureListener { ex ->
                Log.e(TAG, "Listener not registered.", ex)
            }
    }

    private fun removeListener(){
        Fitness.getSensorsClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
            .remove(listener)
            .addOnSuccessListener {
                Log.i(TAG, "Listener was removed!")
            }
            .addOnFailureListener {
                Log.e(TAG, "Listener was not removed.")
            }
    }

    private fun getGoogleAccount() =
        GoogleSignIn.getAccountForExtension(this, fitnessOptions);

    private fun readSavedSteps() {
        Fitness.getHistoryClient(this, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val totalSteps =
                    result.dataPoints.firstOrNull()?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0

                viewModel.setStep(totalSteps)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "There was a problem getting steps.", e)
            }
    }
}
