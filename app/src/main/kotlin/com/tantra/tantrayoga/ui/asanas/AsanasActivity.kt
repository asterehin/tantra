package com.tantra.tantrayoga.ui.asanas

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.databinding.DataBindingUtil
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v7.app.*
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.AsanasActivityBinding
import kotlinx.android.synthetic.main.activity_main.*

fun asanasActivityIntent(context: Context) = Intent(context, AsanasActivity::class.java)

class AsanasActivity : AppCompatActivity() {
    private lateinit var binding: AsanasActivityBinding
    private lateinit var viewModel: AsanasListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.asanas_activity)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(AsanasListViewModel::class.java)

        viewModel.popularMoviesLiveData.observe(this, Observer { posts ->
            viewModel.updateList(posts)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }
}
