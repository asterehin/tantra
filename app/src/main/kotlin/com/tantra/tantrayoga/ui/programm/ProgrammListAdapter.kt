package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.databinding.ProgrammCardBinding
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import kotlinx.android.synthetic.main.programm_card.view.*

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

            val url = "${programmWithAsanas.programm.photoUrl}?w=360" //Append ?w=360 to the URL if the URL is not null. This value assumes that the device screen has 1080px in width. You can set this value dynamically to be one-third of the device’s screen width.
            Glide.with(itemView)
                .load(url)
                .centerCrop() //4
                .placeholder(R.drawable.ic_image_place_holder) //5
                .error(R.drawable.ic_broken_image) //6
                .fallback(R.drawable.ic_no_image) //7
                .transform(CircleCrop()) //4
                .into(itemView.photoProgramm) //8
        }
    }
}