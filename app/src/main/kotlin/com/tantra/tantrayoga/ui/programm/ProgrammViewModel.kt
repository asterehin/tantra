package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.base.BaseViewModel

class ProgrammViewModel: BaseViewModel() {
    private val programmName = MutableLiveData<String>()
    private val programmDesc = MutableLiveData<String>()

    fun bind(post: Programm){
        programmName.value = post.name
        programmDesc.value = post.desc
    }

    fun getProgrammName():MutableLiveData<String>{
        return programmName
    }

    fun getProgrammDesc():MutableLiveData<String>{
        return programmDesc
    }
}