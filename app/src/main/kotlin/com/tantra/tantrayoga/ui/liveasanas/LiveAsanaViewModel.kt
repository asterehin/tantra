package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.LiveAsana

class LiveAsanaViewModel: BaseViewModel() {
    private val asanaName = MutableLiveData<String>()
    private val asanaDesc = MutableLiveData<String>()

    fun bind(liveAsana: LiveAsana){
        asanaName.value = liveAsana.asanaUUID
        asanaDesc.value = liveAsana.programmUUID
    }

    fun getAsanaName():MutableLiveData<String>{
        return asanaName
    }

    fun getAsanaDesc():MutableLiveData<String>{
        return asanaDesc
    }
}