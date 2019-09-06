package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.databinding.DataBindingUtil
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v7.app.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.LiveAsanasActivityBinding
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.ui.asanas.asanasActivityIntent
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

        binding = DataBindingUtil.setContentView(this, R.layout.live_asanas_activity)
//        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, intent.getStringExtra(UUID_KEY)))
            .get(LiveAsanasListViewModel::class.java)

        viewModel.liveAsanasList.observe(this, Observer { LiveAsanaDetails ->
                        viewModel.updateList(LiveAsanaDetails)
        })
        binding.viewModel = viewModel
        binding.handler = viewModel

        supportActionBar?.apply{
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home  -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
