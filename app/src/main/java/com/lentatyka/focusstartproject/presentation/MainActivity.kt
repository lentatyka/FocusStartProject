package com.lentatyka.focusstartproject.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lentatyka.focusstartproject.R
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.databinding.ActivityMainBinding
import com.lentatyka.focusstartproject.di.DaggerAppComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.create().inject(this)
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        setAdapter()
        setViewModel()
        setViews()
    }

    private fun setViews() {

        binding.etValue.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP
            ) {
                binding.result = viewModel.evaluateValue(
                    binding.etValue.text.toString()
                )
                true
            } else
                false
        }
        //verify checkbox preferences (on/off). Add chkboxLiveDate
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, vmFactory)[MainViewModel::class.java]
        viewModel.state.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    binding.swipeLayout.isRefreshing = true
                }
                is State.Error -> {
                    binding.swipeLayout.isRefreshing = false
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                is State.Success -> {
                    with(binding){
                        swipeLayout.isRefreshing = false
                        //pass date to main layout
                        state.data.also {
                            date = it.date
                            previousDate = it.previousDate
                            timestamp = it.timestamp
                        }
                    }

                    mainAdapter.submitList(state.data.rate)
                }
            }
        }
        viewModel.rate.observe(this){
            binding.converter = it
        }
    }

    private fun setAdapter() {
        mainAdapter = MainAdapter { rate ->
            viewModel.setRate(rate)
        }
        with(binding) {
            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recycler.adapter = mainAdapter
            swipeLayout.setOnRefreshListener {
                viewModel.updateExchangeRates()
            }
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}