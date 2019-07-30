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


private const val KIT_ARG = "kit_arg"
private const val GIRL_ID_ARG = "girl_id_arg"

fun asanasActivityIntent(context: Context) = Intent(context, AsanasActivity::class.java)


class AsanasActivity : AppCompatActivity() {
    private lateinit var binding: AsanasActivityBinding
    private lateinit var viewModel: AsanasListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.asanas_activity)
        setSupportActionBar(toolbar)

//        binding.postList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(AsanasListViewModel::class.java)

        viewModel.popularMoviesLiveData.observe(this, Observer { posts ->
            viewModel.updateList(posts)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }

}
