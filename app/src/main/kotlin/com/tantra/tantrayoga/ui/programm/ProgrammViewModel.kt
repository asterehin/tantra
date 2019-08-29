package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import com.tantra.tantrayoga.R


class ProgrammViewModel : BaseViewModel() {
    lateinit var selectedProgramm: MutableLiveData<ProgrammWithAsanas>
    lateinit var context: Context

    private val programmName = MutableLiveData<String>()
    private val programmDesc = MutableLiveData<String>()

    fun bind(programmWithAsanas: ProgrammWithAsanas, context: Context) {
        programmName.value = programmWithAsanas.programm.name
        programmDesc.value = programmWithAsanas.programm.desc
        this.context = context

    }

    fun getProgrammName(): MutableLiveData<String> {
        return programmName
    }

    fun getStatusColor(): Int {
        return ContextCompat.getColor(context, R.color.coins_flat_blue);
    }

    fun getStatusIcon(): Int {
        return R.drawable.ic_material_share
    }

    fun getMinutes(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = "100 minutes"
        return string
    }

    fun getAsanasCount(): MutableLiveData<String> {
        val string = MutableLiveData<String>()
        string.value = "30 asanas"
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
        selectedProgramm.setValue(programmWithAsanas)
    }
}