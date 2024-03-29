package com.tantra.tantrayoga.ui.programm

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import android.app.AlertDialog
import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import com.tantra.tantrayoga.databinding.ActivityProgrammsBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.ui.asanas.asanasActivityIntent
import com.tantra.tantrayoga.ui.liveasanas.liveAsanasActivityIntent
import kotlinx.android.synthetic.main.activity_programms.*
import kotlinx.android.synthetic.main.add_programm_view.view.*
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
        this.getLayoutInflater().inflate(R.layout.add_programm_view, null).apply {

            with(programm) {
                if (!isNew()) {
                    programmNameTextInputEditText.setText(name)
                    programmDescTextInputEditText.setText(desc)

                    val url =
                        "${photoUrl}?w=360" //Append ?w=360 to the URL if the URL is not null. This value assumes that the device screen has 1080px in width. You can set this value dynamically to be one-third of the device’s screen width.
                    Glide.with(this@apply)
                        .load(url)
                        .centerCrop() //4
                        .placeholder(R.drawable.ic_image_place_holder) //5
                        .error(R.drawable.ic_broken_image) //6
                        .fallback(R.drawable.ic_no_image) //7
                        .transform(CircleCrop()) //4
                        .into(photoProgramm) //8
                }
                tagsTextInputEditText.setText(tags)

                if (numOfCycles > 0) {
                    numberOfCyclesEditText.visibility = VISIBLE
                    countLabel.visibility = VISIBLE
                    cyclicSwitch.isChecked = true
                    numberOfCyclesEditText.setText(numOfCycles.toString())
                } else {
                    numberOfCyclesEditText.visibility = GONE
                    countLabel.visibility = GONE
                }
                cyclicSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                    numberOfCyclesEditText.visibility = if (isChecked) VISIBLE else GONE
                    countLabel.visibility = if (isChecked) VISIBLE else GONE
                }

                val dialog = AlertDialog.Builder(c)
                    .setTitle(if (isNew()) "Создаем новую программу" else "Редактируем программу ${name}") //todo amend to string res
                    .setView(this@apply)
                    .setPositiveButton(
                        if (isNew()) "Добавить" else "Изменить"
                    ) { _, which ->
                        name = programmNameTextInputEditText.text.toString()
                        desc = programmDescTextInputEditText.text.toString()
                        tags = tagsTextInputEditText.text.toString()
                        numOfCycles =
                            if (cyclicSwitch.isChecked) numberOfCyclesEditText.text.toString().toInt() else 0
                        viewModel.saveProgramm(this)
                    }
                    .setNegativeButton("Отмена", null)
                    .create()
                dialog.show()
            }
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