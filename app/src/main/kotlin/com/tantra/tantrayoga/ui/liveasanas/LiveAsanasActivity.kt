package com.tantra.tantrayoga.ui.liveasanas

import android.content.*
import androidx.databinding.DataBindingUtil
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreyt.vk.style.factories.AsanasDialogFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.common.dependencyinjection.ViewModelFactory
import com.tantra.tantrayoga.databinding.LiveAsanasActivityBinding
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.widget.ListWithSearchFragment
import kotlinx.android.synthetic.main.add_programm_item_view.*
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

        viewModel.liveAsanasListLiveData.observe(this, Observer { LiveAsanaDetails ->
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
                    itemNameTextView.setOnClickListener{
                        val factory = AsanasDialogFactory(1, "")
                        ListWithSearchFragment.newInstance(factory, ListWithSearchFragment.OnItemSelectedListener {
                            Log.e("LiveAsanasActivity-showAddEditItemDialog 74 ", "")
                            liveAsana.asanaUUID = (it as Asana).UUID
                            asana = it
//                            setAsanaFields(this)
                            itemNameTextView.setText(asana.name)
                            consciousnessDropdownTextView.setContentText(asana.desc)
                            techniqueDropdownTextView.setContentText(asana.technics)
                            effectsDropdownTextView.setContentText(asana.effects)
                        }).show(supportFragmentManager, ListWithSearchFragment.TAG)
                    }
//                    setAsanaFields(this)

                    itemNameTextView.setText(asana.name)
                    consciousnessDropdownTextView.setContentText(asana.desc)
                    techniqueDropdownTextView.setContentText(asana.technics)
                    effectsDropdownTextView.setContentText(asana.effects)
                    with(liveAsana) {
                        audioGuideSwitch.isChecked = playAudio
                        preparationEditText.setText(preparationTime.toString())
                        lengthEditText.setText(lengthTime.toString())
                        consciousnessEditText.setText(consciousnessTime.toString())
                    }

                    val url =
                        "${LiveAsanaDetails.asana.photo}?w=360" //Append ?w=360 to the URL if the URL is not null. This value assumes that the device screen has 1080px in width. You can set this value dynamically to be one-third of the device’s screen width.
                    Glide.with(this@apply)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_place_holder)
                        .error(R.drawable.ic_broken_image)
                        .fallback(R.drawable.ic_no_image)
                        .transform(CircleCrop())
                        .into(photoProgramm)
                }

                val dialog = AlertDialog.Builder(c, R.style.my_dialog)
                    .setTitle(if (isNew()) "Добавляем новую асану" else "Редактируем асану ${asana.name}") //todo amend to string res
                    .setView(this@apply)
                    .setPositiveButton(
                        if (isNew()) "Добавить" else "Изменить"
                    ) { _, which ->
                        with(liveAsana) {
                            playAudio = audioGuideSwitch.isChecked
                            preparationTime = preparationEditText.text.toString().toIntOrNull() ?:0
                            lengthTime = lengthEditText.text.toString().toIntOrNull() ?:0
                            consciousnessTime = consciousnessEditText.text.toString().toIntOrNull() ?:0
                        viewModel.saveLiveAsana(this)
                        }
                    }
                    .setNegativeButton("Отмена", null)
                    .create()
                dialog.show()
            }
        }
    }

    private fun setAsanaFields(        liveAsanaDetails: LiveAsanaDetails
    ) {
        itemNameTextView.setText(liveAsanaDetails.asana.name)
        consciousnessDropdownTextView.setContentText(liveAsanaDetails.asana.desc)
        techniqueDropdownTextView.setContentText(liveAsanaDetails.asana.technics)
        effectsDropdownTextView.setContentText(liveAsanaDetails.asana.effects)
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
