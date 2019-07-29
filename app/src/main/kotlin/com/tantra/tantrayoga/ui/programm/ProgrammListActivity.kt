package com.tantra.tantrayoga.ui.programm

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
import com.tantra.tantrayoga.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.widget.EditText
import android.app.AlertDialog
import android.content.Context
import com.tantra.tantrayoga.databinding.ActivityProgrammsBinding
import com.tantra.tantrayoga.ui.programm.ProgrammListViewModel
import kotlinx.android.synthetic.main.add_new_programm_view.view.*


//https://nuancesprog.ru/p/3270/

class ProgrammListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgrammsBinding
    private lateinit var viewModel: ProgrammListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.activity_programms)
        setSupportActionBar(toolbar)

//        binding.postList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(ProgrammListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        viewModel.popularMoviesLiveData.observe(this, Observer { posts ->
            viewModel.updateList(posts)
        })
        viewModel.navigateToDetails.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                // Only proceed if the event has never been handled
                showAddItemDialog(this)
            }
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }

    private fun showAddItemDialog(c: Context) {
        val addProgrammDialogView = this.getLayoutInflater().inflate(R.layout.add_new_programm_view, null)

        val dialog = AlertDialog.Builder(c)
            .setTitle("Создаем новую программу тренировок")
            .setMessage("Введите название новой программы")
            .setView(addProgrammDialogView)
            .setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, which ->
                    val task = addProgrammDialogView.programmNameEditText.text.toString()
                    val desc = addProgrammDialogView.programmDescEditText.text.toString()
                    viewModel.addNewItem(task, desc)
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