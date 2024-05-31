package com.submission.jktstoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.jktstoryapp.LoadingStateAdapter
import com.submission.jktstoryapp.R
import com.submission.jktstoryapp.StoryListAdapter
import com.submission.jktstoryapp.ViewModelFactory
import com.submission.jktstoryapp.databinding.ActivityMainBinding
import com.submission.jktstoryapp.ui.addStory.AddStoryActivity
import com.submission.jktstoryapp.ui.login.LoginActivity
import com.submission.jktstoryapp.ui.map.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyListAdapter: StoryListAdapter


    private val mainViewmodel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()

        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)

        getAllStory()

        mainViewmodel.isLoading.observe(this) {
            showLoading(it)
        }


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllStory() {
        val adapter = StoryListAdapter()
        binding.recyclerViewMain.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewmodel.stories.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_map -> {
                moveToMap()
                true
            }

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
        getAllStory()
    }

    private fun checkLogin() {
        mainViewmodel.getSession().observe(this) { session ->
            if (session == null || session.token?.isEmpty() != false) {
                moveToLogin()
            } else {
                getAllStory()
            }
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun moveToMap() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}