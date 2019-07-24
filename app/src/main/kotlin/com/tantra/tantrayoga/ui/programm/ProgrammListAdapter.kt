package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.ProgrammCardBinding
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.ui.programm.ProgrammViewModel

class ProgrammListAdapter: RecyclerView.Adapter<ProgrammListAdapter.ViewHolder>() {
    private lateinit var postList:List<Programm>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProgrammCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.programm_card, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return if(::postList.isInitialized) postList.size else 0
    }

    fun updateProgrammList(postList:List<Programm>){
        this.postList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ProgrammCardBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = ProgrammViewModel()

        fun bind(post:Programm){
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}