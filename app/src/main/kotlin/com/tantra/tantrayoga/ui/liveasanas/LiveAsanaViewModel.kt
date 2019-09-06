package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View.VISIBLE
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.model.ProgrammWithAsanas

class LiveAsanaViewModel : BaseViewModel() {
    private val asanaName = MutableLiveData<String>()
    private val asanaDesc = MutableLiveData<String>()

    fun bind(liveAsana: LiveAsanaDetails) {
        asanaName.value = liveAsana.asana.name
//        asanaDesc.value = "%d".format(liveAsana.liveAsana.consciousnessTime)
        asanaDesc.value = liveAsana.asana.desc
    }

    fun getAsanaName(): MutableLiveData<String> {
        return asanaName
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
        "a" -> R.drawable.ic_material_person
        "p" -> R.drawable.ic_material_person
        "b" -> R.drawable.ic_material_person
        else -> R.drawable.ic_material_person
    }

    fun deleteButtonVisibility(liveAsanaDetails: LiveAsanaDetails) = VISIBLE
    //if (programmWithAsanas.isPersonal()) VISIBLE else GONE

    fun getMinutes(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = "100 minutes"
        return string
    }

    fun getCiclesCount(context: Context, liveAsanaDetails: LiveAsanaDetails): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = context.getString(R.string.count_of_cicles_label, 5)
        return string
    }

    fun onItemClick(liveAsanaDetails: LiveAsanaDetails) {
//        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.SELECT))
    }

    fun onDetails(liveAsanaDetails: LiveAsanaDetails) {
//        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.DELETE))
    }

    fun onDelete(liveAsanaDetails: LiveAsanaDetails) {
//        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.DELETE))
    }

    fun onEdit(liveAsanaDetails: LiveAsanaDetails) {
//        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.EDIT))
    }
}