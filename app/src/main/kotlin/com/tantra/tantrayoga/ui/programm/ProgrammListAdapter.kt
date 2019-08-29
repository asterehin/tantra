package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.ProgrammCardBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.ProgrammWithAsanas

class ProgrammListAdapter : RecyclerView.Adapter<ProgrammListAdapter.ViewHolder>() {
    private lateinit var mutableList: MutableList<ProgrammWithAsanas>
    lateinit var onProgrammActionEvent: MutableLiveData<Event<ProgrammWithAsanas>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProgrammCardBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.programm_card, parent, false)
        return ViewHolder(binding, onProgrammActionEvent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mutableList[position])
    }

    override fun getItemCount(): Int {
        return if (::mutableList.isInitialized) mutableList.size else 0
    }

    fun updateProgrammList(mutableList: MutableList<ProgrammWithAsanas>) {
        this.mutableList = mutableList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ProgrammCardBinding,
        var onProgrammActionEvent:  MutableLiveData<Event<ProgrammWithAsanas>>
    ) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ProgrammViewModel()

        fun bind(programmWithAsanas: ProgrammWithAsanas) {

            viewModel.bind(programmWithAsanas)
            binding.obj = programmWithAsanas
            binding.viewModel = viewModel
            viewModel.onProgrammActionEvent = onProgrammActionEvent
        }
    }
}