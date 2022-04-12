package com.lentatyka.focusstartproject.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lentatyka.focusstartproject.FocusStartApplication
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
        (application as FocusStartApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        setAdapter()
        setViewModel()
        setViews()
    }

    private fun setViews() {
        with(binding){
            etValue.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(chars: CharSequence, p1: Int, p2: Int, p3: Int) {
                    viewModel.evaluateValue(chars.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
            with(chbAutoUpdate){
                isChecked = viewModel.isChecked
                setOnClickListener {
                    viewModel.setAutoUpdate(this.isChecked)
                }
            }
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, vmFactory)[MainViewModel::class.java]
        //
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
                    with(binding) {
                        swipeLayout.isRefreshing = false
                        //pass date to main layout
                        state.data.also {
                            date = it.date
                            previousDate = it.previousDate
                            timestamp = it.timestamp
                        }
                        //update result
                        viewModel.evaluateValue(etValue.text.toString())
                    }

                    mainAdapter.submitList(state.data.rate)
                }
            }
        }
        //
        viewModel.rate.observe(this) {
            with(binding) {
                converter = it
                //update result
                viewModel.evaluateValue(etValue.text.toString())
            }
        }
        //
        viewModel.result.observe(this) {
            binding.result = it
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