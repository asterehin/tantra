package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.LiveAsanaCardBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import kotlinx.android.synthetic.main.live_asana_card.view.*
import kotlinx.android.synthetic.main.programm_card.view.*

class LiveAsanasListAdapter: RecyclerView.Adapter<LiveAsanasListAdapter.ViewHolder>() {
    private lateinit var liveAsanas:List<LiveAsanaDetails>
    lateinit var onItemActionEvent: MutableLiveData<Event<LiveAsanaDetails>>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LiveAsanaCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.live_asana_card, parent, false)
        return ViewHolder(binding,onItemActionEvent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(liveAsanas[position])
    }

    override fun getItemCount(): Int {
        return if(::liveAsanas.isInitialized) liveAsanas.size else 0
    }

    fun updateLiveAsanaList(list:List<LiveAsanaDetails>){
        this.liveAsanas = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: LiveAsanaCardBinding,
                     var onItemActionEvent:  MutableLiveData<Event<LiveAsanaDetails>>):RecyclerView.ViewHolder(binding.root){
        private val viewModel = LiveAsanaViewModel()

        fun bind(liveAsanaDetails:LiveAsanaDetails){
            viewModel.bind(liveAsanaDetails)
            binding.viewModel = viewModel
            binding.obj = liveAsanaDetails
            viewModel.onItemActionEvent = onItemActionEvent


            val url = "${liveAsanaDetails.asana.photo}?w=360" //Append ?w=360 to the URL if the URL is not null. This value assumes that the device screen has 1080px in width. You can set this value dynamically to be one-third of the device’s screen width.
            Glide.with(itemView)
                .load(url)
                .centerCrop() //4
                .placeholder(R.drawable.ic_image_place_holder) //5
                .error(R.drawable.ic_broken_image) //6
                .fallback(R.drawable.ic_no_image) //7
                .transform(CircleCrop()) //4
                .into(itemView.photoAsana) //8
        }
    }
}