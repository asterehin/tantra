package com.tantra.tantrayoga.ui.asanas

import android.content.*
import androidx.databinding.DataBindingUtil
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStoreOwner
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.AsanasActivityBinding
import kotlinx.android.synthetic.main.activity_programms.*

fun asanasActivityIntent(context: Context) = Intent(context, AsanasActivity::class.java)

class AsanasActivity : AppCompatActivity(), ViewModelStoreOwner {
    private lateinit var binding: AsanasActivityBinding
    private lateinit var viewModel: AsanasListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.asanas_activity)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(AsanasListViewModel::class.java)

        viewModel.popularMoviesLiveData.observe(this, Observer { posts ->
            viewModel.updateList(posts)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }
}
