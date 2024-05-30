package com.submission.jktstoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.jktstoryapp.R
import com.submission.jktstoryapp.StoryListAdapter
import com.submission.jktstoryapp.ViewModelFactory
import com.submission.jktstoryapp.databinding.ActivityMainBinding
import com.submission.jktstoryapp.ui.addStory.AddStoryActivity
import com.submission.jktstoryapp.ui.login.LoginActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyListAdapter: StoryListAdapter


    private val mainViewmodel by viewModels<MainViewmodel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()
        setupRecyclerView()


        mainViewmodel.stories.observe(this) { stories ->
            storyListAdapter.submitList(stories)
        }

        mainViewmodel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewmodel.getAllStories(this)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewmodel.logout()
                moveToLogin()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewmodel.getAllStories(this)
    }

    private fun checkLogin() {
        mainViewmodel.getSession().observe(this) {session ->
            if (session == null || session.token?.isEmpty() != false) {
                moveToLogin()
            } else {
                mainViewmodel.getAllStories(this)
            }
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun setupRecyclerView() {
        storyListAdapter = StoryListAdapter()
        binding.recyclerViewMain.apply {
            adapter = storyListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}