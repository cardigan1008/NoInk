package com.bagel.noink.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.GridLayout
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bagel.noink.R
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.ActivitySearchBinding
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.HttpRequest
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

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

    @SuppressLint("ResourceType")
    private fun performSearch(query: String, moodTags: ArrayList<String>, eventTags: ArrayList<String>) {
        Log.i("SearchActivity", "Search query: $query, event tags: $eventTags, mood tags: $moodTags")

        val callbackListener = object : HttpRequest.CallbackListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(responseJson: JSONObject) {
                val items = responseJson.getJSONArray("data")
                val searchList: ArrayList<ListItemBean> = ArrayList()

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val uriStrList: List<String> = item.getString("imageUrl").split(",")
                    val uriList: ArrayList<Uri> = ArrayList()

                    for (uriStr in uriStrList) {
                        val uri = Uri.parse(uriStr)
                        uriList.add(uri)
                    }

                    val dateString = item.getString("createdAt")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = dateFormat.parse(dateString)

                    searchList.add(
                        ListItemBean(
                            item.getInt("rid"),
                            item.getString("title"),
                            item.getString("generatedText"),
                            uriList[0],
                            uriList,
                            date
                        )
                    )
                }

                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

                val bundle = Bundle()
                bundle.putParcelableArrayList("searchList", searchList)
                navHostFragment?.findNavController()?.navigate(R.id.nav_search_result, bundle)
            }

            override fun onFailure(errorMessage: String) {
                Log.i("HTTPResponse", errorMessage)
                print(errorMessage)
            }
        }

        val queryString = URLEncoder.encode(query, "UTF-8")
        val moodTagsString = moodTags.joinToString(",") { URLEncoder.encode(it, "UTF-8") }
        val eventTagsString = eventTags.joinToString(",") { URLEncoder.encode(it, "UTF-8") }
        val httpRequest = HttpRequest()
        httpRequest.post(Contants.SERVER_ADDRESS +
                "/api/record/records?search=$queryString&labelList=${moodTagsString}&typeList=${eventTagsString}", RequestBody.create(null, byteArrayOf()), "satoken", AccountViewModel.token!!, callbackListener)
        Log.i("HTTPResponse", Contants.SERVER_ADDRESS +
                "/api/record/records?search=$queryString&labelList=${moodTagsString}&typeList=${eventTagsString}")
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