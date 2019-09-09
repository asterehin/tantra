package com.tantra.tantrayoga.ui.asanas

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.AsanaCardBinding
import com.tantra.tantrayoga.model.Asana

class AsanasListAdapter: RecyclerView.Adapter<AsanasListAdapter.ViewHolder>() {
    private lateinit var postList:List<Asana>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AsanaCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.asana_card, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return if(::postList.isInitialized) postList.size else 0
    }

    fun updateAsanaList(postList:List<Asana>){
        this.postList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: AsanaCardBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = AsanaViewModel()

        fun bind(post:Asana){
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}