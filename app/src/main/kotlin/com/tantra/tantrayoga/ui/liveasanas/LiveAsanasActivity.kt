package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.databinding.DataBindingUtil
import android.os.*
import android.support.v7.app.*
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.LiveAsanasActivityBinding
import kotlinx.android.synthetic.main.activity_main.*

fun liveAsanasActivityIntent(context: Context) = Intent(context, LiveAsanasActivity::class.java)

class LiveAsanasActivity : AppCompatActivity() {
    private lateinit var binding: LiveAsanasActivityBinding
    private lateinit var viewModel: LiveAsanasListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.live_asanas_activity)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LiveAsanasListViewModel::class.java)

        viewModel.popularMoviesLiveData.observe(this, Observer { LiveAsana ->
            viewModel.updateList(LiveAsana)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }
}
