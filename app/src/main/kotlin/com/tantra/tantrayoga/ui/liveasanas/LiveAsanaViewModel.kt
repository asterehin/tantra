package com.tantra.tantrayoga.ui.liveasanas

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.View.VISIBLE
import androidx.lifecycle.MutableLiveData
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.model.ProgrammWithAsanas

class LiveAsanaViewModel : BaseViewModel() {
    private val asanaName = MutableLiveData<String>()
    private val sanscritName = MutableLiveData<String>()
    private val asanaDesc = MutableLiveData<String>()
    lateinit var onItemActionEvent: MutableLiveData<Event<LiveAsanaDetails>>
    private lateinit var liveAsana: LiveAsana


    fun bind(liveAsana: LiveAsanaDetails) {
        this.liveAsana = liveAsana.liveAsana
        asanaName.value = liveAsana.asana?.name
        sanscritName.value = liveAsana.asana?.sanscritName
//        asanaDesc.value = "%d".format(liveAsana.liveAsana.consciousnessTime)
        asanaDesc.value = liveAsana.asana?.desc
    }

    fun getAsanaName(): MutableLiveData<String> {
        return asanaName
    }

    fun getSanscritAsanaName(): MutableLiveData<String> {
        return sanscritName
    }

    fun getAsanaDesc(): MutableLiveData<String> {
        return asanaDesc
    }

    fun getItemType(context: Context, liveAsanaDetails: LiveAsanaDetails) = when (liveAsanaDetails.liveAsana.type) {
        "a" -> context.getString(R.string.asana_label)
        "p" -> context.getString(R.string.programm_label)
        "b" -> context.getString(R.string.pranayama_label)
        else -> ""
    }

    fun getStatusColor(context: Context, liveAsanaDetails: LiveAsanaDetails) = ContextCompat.getColor(
        context, when (liveAsanaDetails.liveAsana.type) {
            "a" -> R.color.coins_flat_green
            "p" -> R.color.coins_flat_orange
            "b" -> R.color.coins_flat_blue
            else -> R.color.coins_flat_blue
        }
    )

    fun getStatusIcon(liveAsanaDetails: LiveAsanaDetails) = when (liveAsanaDetails.liveAsana.type) {
        "a" -> R.drawable.ic_yoga_pose
        "p" -> R.drawable.ic_awesome_list_ol
        "b" -> R.drawable.ic_lungs
        else -> R.drawable.ic_material_person
    }

    fun deleteButtonVisibility(liveAsanaDetails: LiveAsanaDetails) = VISIBLE
    //if (programmWithAsanas.isPersonal()) VISIBLE else GONE

    fun getMinutes(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = liveAsana.getSummaryTime().toString()
        return string
    }

    fun getCiclesCount(context: Context, liveAsanaDetails: LiveAsanaDetails): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = context.getString(R.string.count_of_cicles_label, 5)
        return string
    }

    fun onItemClick(liveAsanaDetails: LiveAsanaDetails) {
        onItemActionEvent.setValue(Event(liveAsanaDetails, Event.Action.EDIT))
    }

    fun onDetails(liveAsanaDetails: LiveAsanaDetails) {
        onItemActionEvent.setValue(Event(liveAsanaDetails, Event.Action.EDIT))
    }

    fun onDelete(liveAsanaDetails: LiveAsanaDetails) {
        onItemActionEvent.setValue(Event(liveAsanaDetails, Event.Action.DELETE))
    }

    fun onEdit(liveAsanaDetails: LiveAsanaDetails) {
        onItemActionEvent.setValue(Event(liveAsanaDetails, Event.Action.EDIT))
    }
}