package com.lentatyka.focusstartproject.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lentatyka.focusstartproject.R
import com.lentatyka.focusstartproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        setAdapter()
        setViewModel()
    }

    private fun setViewModel() {
        TODO("Not yet implemented")
    }

    private fun setAdapter() {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}