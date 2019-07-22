package com.tantra.tantrayoga.ui.post

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.common.dependencyinjection.component.DaggerApplicationComponent
import com.tantra.tantrayoga.common.dependencyinjection.module.ApplicationModule
import com.tantra.tantrayoga.common.dependencyinjection.module.RoomModule
import com.tantra.tantrayoga.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.widget.EditText
import android.app.AlertDialog
import android.content.Context


//https://nuancesprog.ru/p/3270/

class PostListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PostListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.activity_main)
        setSupportActionBar(toolbar)

        binding.postList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(PostListViewModel::class.java)
        viewModel.addLocationUpdates(this)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        viewModel.popularMoviesLiveData.observe(this, Observer { posts ->
            viewModel.updateList(posts)
        })
        viewModel.navigateToDetails.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                Log.e("PostListActivity-onCreate 48 ", "trigger the opening the dialog")
                showAddItemDialog(this)
            }
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }

    private fun showAddItemDialog(c: Context) {
        val taskEditText = EditText(c)
        val dialog = AlertDialog.Builder(c)
            .setTitle("Создаем новую программу тренировок")
            .setMessage("Введите название новой программы")
            .setView(taskEditText)
            .setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, which -> val task = taskEditText.text.toString()
                Log.e("PostListActivity-showAddItemDialog 69 ", "" + task)
                    viewModel.addNewItem(task)

                })
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(com.tantra.tantrayoga.R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.tantra.tantrayoga.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            com.tantra.tantrayoga.R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}