package com.submission.jktstoryapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.databinding.ListStoryBinding
import com.submission.jktstoryapp.ui.detail.DetailListActivity

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }


    inner class MyViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var storyImage = binding.imageView
        private var listTitle = binding.listTitle
        private var listDesc = binding.listDesc

        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(storyImage, "image"),
                Pair(listTitle, "title"),
                Pair(listDesc, "desc")
            )

        fun bind(story: ListStoryItem) {
            binding.listTitle.text = story.name
            binding.listDesc.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imageView)
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
                val intent = Intent(itemView.context, DetailListActivity::class.java)
                intent.putExtra("Story", story)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}