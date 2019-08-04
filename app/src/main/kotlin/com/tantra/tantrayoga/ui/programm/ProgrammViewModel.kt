package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import android.util.Log


class ProgrammViewModel : BaseViewModel() {
    lateinit var selectedProgramm: MutableLiveData<ProgrammWithAsanas>

    private val programmName = MutableLiveData<String>()
    private val programmDesc = MutableLiveData<String>()

    fun bind(programmWithAsanas: ProgrammWithAsanas) {
        programmName.value = programmWithAsanas.programm.name
        programmDesc.value = programmWithAsanas.programm.desc
    }

    fun getProgrammName(): MutableLiveData<String> {
        return programmName
    }

    fun getProgrammDesc(): MutableLiveData<String> {
        return programmDesc
    }

    fun onItemClick(programmWithAsanas: ProgrammWithAsanas) {
        selectedProgramm.setValue(programmWithAsanas)
    }
}