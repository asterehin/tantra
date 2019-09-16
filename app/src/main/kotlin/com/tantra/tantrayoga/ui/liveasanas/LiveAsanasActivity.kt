package com.tantra.tantrayoga.ui.liveasanas

import android.content.*
import androidx.databinding.DataBindingUtil
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.LiveAsanasActivityBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.ui.asanas.asanasActivityIntent
import kotlinx.android.synthetic.main.add_programm_item_view.view.*

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

        viewModel.onItemActionEvent.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { action ->
                when (action.action) {
                    // Only proceed if the event has never been handled
                    Event.Action.SELECT -> showAddEditItemDialog(this, action.content)
//                    Event.Action.DELETE -> viewModel.deleteProgramm(action.content)
                    Event.Action.EDIT -> showAddEditItemDialog(this, action.content)
                }
            }
        })
        binding.viewModel = viewModel
        binding.handler = viewModel

        supportActionBar?.apply{
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }
    private fun showAddEditItemDialog(c: Context, LiveAsanaDetails: LiveAsanaDetails) {
        this.getLayoutInflater().inflate(R.layout.add_programm_item_view, null).apply {

            with(LiveAsanaDetails) {
                if (!isNew()) {
                    itemNameTextView.setText(asana.name)
                    consciousnessDropdownTextView.setContentText(asana.desc)
                    techniqueDropdownTextView.setContentText(asana.technics)
                    effectsDropdownTextView.setContentText(asana.effects)

                    val url =
                        "${LiveAsanaDetails.asana.photo}?w=360" //Append ?w=360 to the URL if the URL is not null. This value assumes that the device screen has 1080px in width. You can set this value dynamically to be one-third of the device’s screen width.
                    Glide.with(this@apply)
                        .load(url)
                        .centerCrop() //4
                        .placeholder(R.drawable.ic_image_place_holder) //5
                        .error(R.drawable.ic_broken_image) //6
                        .fallback(R.drawable.ic_no_image) //7
                        .transform(CircleCrop()) //4
                        .into(photoProgramm) //8
                }
//                tagsTextInputEditText.setText(asana.tags)

//                if (numOfCycles > 0) {
//                    numberOfCyclesEditText.visibility = VISIBLE
//                    countLabel.visibility = VISIBLE
//                    cyclicSwitch.isChecked = true
//                    numberOfCyclesEditText.setText(numOfCycles.toString())
//                } else {
//                    numberOfCyclesEditText.visibility = GONE
//                    countLabel.visibility = GONE
//                }
//                cyclicSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//                    numberOfCyclesEditText.visibility = if (isChecked) VISIBLE else GONE
//                    countLabel.visibility = if (isChecked) VISIBLE else GONE
//                }

                val dialog = AlertDialog.Builder(c, R.style.my_dialog)
                    .setTitle(if (isNew()) "Добавляем новую асану" else "Редактируем асану ${asana.name}") //todo amend to string res
                    .setView(this@apply)
                    .setPositiveButton(
                        if (isNew()) "Добавить" else "Изменить"
                    ) { _, which ->
//                        name = programmNameEditText.text.toString()
//                        desc = programmDescEditText.text.toString()
//                        tags = tagsEditText.text.toString()
//                        numOfCycles =
//                            if (cyclicSwitch.isChecked) numberOfCyclesEditText.text.toString().toInt() else 0
//                        viewModel.saveProgramm(this)
                    }
                    .setNegativeButton("Отмена", null)
                    .create()
                dialog.show()
            }
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
