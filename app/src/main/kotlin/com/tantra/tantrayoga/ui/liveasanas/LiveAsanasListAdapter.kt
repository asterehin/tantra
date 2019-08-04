package com.tantra.tantrayoga.ui.liveasanas

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.LiveAsanaCardBinding
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.ui.asanas.AsanasListAdapter

class LiveAsanasListAdapter: RecyclerView.Adapter<LiveAsanasListAdapter.ViewHolder>() {
    private lateinit var liveAsanas:List<LiveAsana>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LiveAsanaCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.live_asana_card, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(liveAsanas[position])
    }

    override fun getItemCount(): Int {
        return if(::liveAsanas.isInitialized) liveAsanas.size else 0
    }

    fun updateLiveAsanaList(postList:List<LiveAsana>){
        this.liveAsanas = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: LiveAsanaCardBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = LiveAsanaViewModel()

        fun bind(post:LiveAsana){
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}