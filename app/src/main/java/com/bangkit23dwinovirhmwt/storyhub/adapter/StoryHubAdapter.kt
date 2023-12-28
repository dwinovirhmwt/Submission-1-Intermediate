package com.bangkit23dwinovirhmwt.storyhub.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23dwinovirhmwt.storyhub.data.local.entity.StoryEntity
import com.bangkit23dwinovirhmwt.storyhub.databinding.ListItemsBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class StoryHubAdapter : ListAdapter<StoryEntity, StoryHubAdapter.StoryHubViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHubViewHolder {
        val binding = ListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryHubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryHubViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

    class StoryHubViewHolder(private val binding: ListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: StoryEntity?) {
            binding.apply {
                Glide.with(itemView.context).load(stories?.photoUrl).transform(
                    CenterCrop(),
                    RoundedCorners(30)
                ).into(ivImage)
                tvName.text = stories?.name
                tvDesc.text = stories?.description
            }

            itemView.setOnClickListener {
                val optionsCompact: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivImage, "story_image"),
                        Pair(binding.tvName, "story_name")
                    )

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("id", stories?.id)
                itemView.context.startActivity(intent, optionsCompact.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: StoryEntity,
                    newItem: StoryEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}