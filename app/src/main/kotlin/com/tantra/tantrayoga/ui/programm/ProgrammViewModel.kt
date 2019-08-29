package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.model.Event

class ProgrammViewModel : BaseViewModel() {
    lateinit var onProgrammActionEvent: MutableLiveData<Event<ProgrammWithAsanas>>

    private val programmName = MutableLiveData<String>()
    private val programmDesc = MutableLiveData<String>()

    fun bind(programmWithAsanas: ProgrammWithAsanas) {
        programmName.value = programmWithAsanas.programm.name
        programmDesc.value = programmWithAsanas.programm.desc
    }

    fun getProgrammName(): MutableLiveData<String> {
        return programmName
    }

    fun getSchoolName(context: Context, programmWithAsanas: ProgrammWithAsanas): String {
        return if (programmWithAsanas.isPersonal()) context.getString(R.string.personal_label) else "Сатья"
    }

    fun getStatusColor(context: Context, programmWithAsanas: ProgrammWithAsanas) = ContextCompat.getColor(
        context,
        if (programmWithAsanas.isPersonal()) R.color.coins_flat_blue else R.color.coins_flat_orange
    )

    fun getStatusIcon(programmWithAsanas: ProgrammWithAsanas) =
        if (programmWithAsanas.isPersonal()) R.drawable.ic_material_person else R.drawable.ic_material_share

    fun deleteButtonVisibility(programmWithAsanas: ProgrammWithAsanas) =
        if (programmWithAsanas.isPersonal()) VISIBLE else GONE

    fun getMinutes(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = "100 minutes"
        return string
    }

    fun getAsanasCount(context: Context, programmWithAsanas: ProgrammWithAsanas): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = context.getString(R.string.count_of_asanas_label, programmWithAsanas.liveAsanas.count())
        return string
    }

    fun getPranayamasCount(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = "30 pranayamas"
        return string
    }

    fun getProgrammDesc(): MutableLiveData<String> {
        return programmDesc
    }

    fun onItemClick(programmWithAsanas: ProgrammWithAsanas) {
        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.SELECT))
    }

    fun onDelete(programmWithAsanas: ProgrammWithAsanas) {
        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.DELETE))
    }

    fun onEdit(programmWithAsanas: ProgrammWithAsanas) {
        onProgrammActionEvent.setValue(Event(programmWithAsanas, Event.Action.EDIT))
    }
}