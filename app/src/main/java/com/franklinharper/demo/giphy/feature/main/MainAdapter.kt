package com.franklinharper.demo.giphy.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.franklinharper.demo.giphy.R
import com.franklinharper.demo.giphy.data.domain.Gif
import com.franklinharper.demo.giphy.databinding.ItemBinding
import timber.log.Timber

class MainAdapter : ListAdapter<Gif, MainAdapter.GifViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            /* attachToParent = */ false,
        )
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        // Delegating binding to the ViewHolder makes it easier to scale when there are
        // multiple types of ViewHolders.
        //
        // Another solution is to use something like Epoxy. It uses code generation to
        // simplify implementing complex RecyclerViews.
        // For details see: https://github.com/airbnb/epoxy
        //
        // Or use Compose instead of a RecyclerView.
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gif>() {

            override fun areItemsTheSame(
                oldItem: Gif,
                newItem: Gif
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Gif,
                newItem: Gif
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class GifViewHolder(
        private val binding: ItemBinding,
        // If we needed to handle item clicks we would pass in a click handler.
        // private val onItemClick: ((Gif) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

//        init {
//            itemView.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    onItemClick?.invoke(getItem(adapterPosition))
//                }
//            }
//        }

        fun bind(gif: Gif) {
            Timber.d("loading: ${gif.url}")
            Glide
                .with(this.itemView.context)
                .load(gif.url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageView)
            binding.headline.text = gif.headline
        }
    }
}
