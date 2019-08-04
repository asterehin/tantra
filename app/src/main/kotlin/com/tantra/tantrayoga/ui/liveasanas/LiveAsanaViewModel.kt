package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.LiveAsanaDetails

class LiveAsanaViewModel: BaseViewModel() {
    private val asanaName = MutableLiveData<String>()
    private val asanaDesc = MutableLiveData<String>()

    fun bind(liveAsana: LiveAsanaDetails){
        asanaName.value = liveAsana.asana.name
        asanaDesc.value = "%d".format(liveAsana.liveAsana.consciousnessTime)
    }

    fun getAsanaName():MutableLiveData<String>{
        return asanaName
    }

    fun getAsanaDesc():MutableLiveData<String>{
        return asanaDesc
    }
}