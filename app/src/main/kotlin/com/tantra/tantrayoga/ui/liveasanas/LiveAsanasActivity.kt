package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.databinding.DataBindingUtil
import android.os.*
import android.support.v7.app.*
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.LiveAsanasActivityBinding
import com.tantra.tantrayoga.model.LiveAsana
import kotlinx.android.synthetic.main.activity_main.*

private const val UUID_KEY = "uuid_key"
fun liveAsanasActivityIntent(context: Context, uuid: String): Intent {
    val intent = Intent(context, LiveAsanasActivity::class.java)
    intent.putExtra(UUID_KEY, uuid)
    return intent
}

class LiveAsanasActivity : AppCompatActivity() {
    private lateinit var binding: LiveAsanasActivityBinding
    private lateinit var viewModel: LiveAsanasListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.live_asanas_activity)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, intent.getStringExtra(UUID_KEY)))
            .get(LiveAsanasListViewModel::class.java)

        viewModel.liveAsanasList.observe(this, Observer { LiveAsanaDetails ->
                        viewModel.updateList(LiveAsanaDetails)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }
}
