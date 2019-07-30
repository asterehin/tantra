package com.tantra.tantrayoga.ui.asanas

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.base.BaseViewModel

class AsanaViewModel: BaseViewModel() {
    private val asanaName = MutableLiveData<String>()
    private val asanaDesc = MutableLiveData<String>()

    fun bind(post: Asana){
        asanaName.value = post.name
        asanaDesc.value = post.desc
    }

    fun getAsanaName():MutableLiveData<String>{
        return asanaName
    }

    fun getAsanaDesc():MutableLiveData<String>{
        return asanaDesc
    }
}