package com.bagel.noink.activity

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.GridLayout
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.bagel.noink.R
import com.bagel.noink.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding

    lateinit var gridLayout: GridLayout

    var selectedMoodTags : ArrayList<String> = ArrayList()
    var selectedEventTags : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gridLayout = findViewById(R.id.mood_category)
        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i)

            if (view is ToggleButton) {
                view.setOnCheckedChangeListener { _, isChecked ->
                    val tag = view.text.toString()
                    if (isChecked) {
                        selectedMoodTags.add(tag)
                    } else {
                        selectedMoodTags.remove(tag)
                    }
                }
            }
        }

        gridLayout = findViewById(R.id.event_category)
        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i)

            if (view is ToggleButton) {
                view.setOnCheckedChangeListener { _, isChecked ->
                    val tag = view.text.toString()
                    if (isChecked) {
                        selectedEventTags.add(tag)
                    } else {
                        selectedEventTags.remove(tag)
                    }
                }
            }
        }

        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            resetSelection()
        }

        val confirmButton: Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            performSearch()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.backButton.setOnClickListener{
            finish()
        }
    }

    private fun performSearch() {
        val searchQuery = binding.searchEditText.text.toString()
        performSearch(searchQuery, selectedMoodTags, selectedEventTags)
    }

    private fun performSearch(query: String, moodTags: ArrayList<String>, eventTags: ArrayList<String>) {
        Log.i("SearchActivity", "Search query: $query, event tags: $eventTags, mood tags: $moodTags")
        // TODO: backend api
    }

    private fun resetSelection() {
        // 清空已选选项
        selectedMoodTags.clear()
        selectedEventTags.clear()

        // 更新视图状态
        updateToggleButtonStates(findViewById(R.id.mood_category), selectedMoodTags)
        updateToggleButtonStates(findViewById(R.id.event_category), selectedEventTags)
    }
    private fun updateToggleButtonStates(layout: GridLayout, selectedTags: ArrayList<String>) {
        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)
            if (view is ToggleButton) {
                val tag = view.text.toString()
                view.isChecked = selectedTags.contains(tag)
            }
        }
    }
}