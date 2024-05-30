package com.submission.jktstoryapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.databinding.ActivityDetailListBinding

class DetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
    }

    private fun setupData() {
        val detailStory = intent.getParcelableExtra<ListStoryItem>("Story") as ListStoryItem
        Glide.with(applicationContext)
            .load(detailStory.photoUrl)
            .into(binding.storyImage)
        binding.tvDetailDesc.text = detailStory.description
        binding.tvDetailTitle.text = detailStory.name
    }
}