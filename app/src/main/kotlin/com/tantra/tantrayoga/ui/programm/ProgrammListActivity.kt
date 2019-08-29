package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog
import android.content.Context
import com.tantra.tantrayoga.databinding.ActivityProgrammsBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.ui.asanas.asanasActivityIntent
import com.tantra.tantrayoga.ui.liveasanas.liveAsanasActivityIntent
import kotlinx.android.synthetic.main.add_new_programm_view.view.*
import java.util.*


//https://nuancesprog.ru/p/3270/

class ProgrammListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgrammsBinding
    private lateinit var viewModel: ProgrammListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.tantra.tantrayoga.R.layout.activity_programms)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(ProgrammListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        viewModel.programmsWithAsanasLiveData.observe(this, Observer { programmsWithAsanas ->
            viewModel.updateList(programmsWithAsanas!!)
        })
//        viewModel.selectedProgramm.observe(this, Observer { programmsWithAsanas ->
//            startActivity(liveAsanasActivityIntent(this, programmsWithAsanas!!.programm.UUID))
//        })
        viewModel.onProgrammActionEvent.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { actionProgramm ->
                when (actionProgramm.action) {
                    // Only proceed if the event has never been handled
                    Event.Action.SELECT -> startActivity(
                        liveAsanasActivityIntent(
                            this,
                            actionProgramm.content.programm.UUID
                        )
                    )
                    Event.Action.DELETE -> viewModel.deleteProgramm(actionProgramm.content)
                    Event.Action.EDIT -> showAddEditProgrammDialog(this, actionProgramm.content.programm)
                }
            }
        })
        viewModel.tapOnAddFab.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                // Only proceed if the event has never been handled
                showAddEditProgrammDialog(this, Programm(0, "andter", "", UUID.randomUUID().toString()))
            }
        })
        binding.viewModel = viewModel
        binding.handler = viewModel
    }

    private fun showAddEditProgrammDialog(c: Context, programm: Programm) {
        val addProgrammDialogView = this.getLayoutInflater().inflate(R.layout.add_new_programm_view, null)

        programm.apply {
            if (!isNew()) {
                addProgrammDialogView.programmNameEditText.setText(name)
                addProgrammDialogView.programmDescEditText.setText(desc)
            }
            val dialog = AlertDialog.Builder(c)
                .setTitle(if (isNew()) "Создаем новую программу" else "Редактируем программу ${name}")
                .setView(addProgrammDialogView)
                .setPositiveButton(if (isNew()) "Добавить" else "Изменить"
                ) { _, which ->
                    name = addProgrammDialogView.programmNameEditText.text.toString()
                    desc = addProgrammDialogView.programmDescEditText.text.toString()
                    viewModel.saveProgramm(this)
                }
                .setNegativeButton("Отмена", null)
                .create()
            dialog.show()
        }
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
            com.tantra.tantrayoga.R.id.action_asanas -> {
                startActivity(asanasActivityIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}