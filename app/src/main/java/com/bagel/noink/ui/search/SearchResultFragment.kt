package com.bagel.noink.ui.search

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.SearchAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentSearchResultBinding
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.HttpRequest
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

class SearchResultFragment : Fragment(R.layout.fragment_search_result) {

    private var _binding: FragmentSearchResultBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SearchAdapter? = null

    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        addRecycleView(root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecycleView(view: View) {
        recyclerView = view.findViewById(R.id.search_recycle)
        adapter = SearchAdapter(performSearch(
            arguments?.getString("searchQuery")!!,
            arguments?.getString("moodTags")!!,
            arguments?.getString("eventTags")!!
        ))
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    @SuppressLint("ResourceType")
    private fun performSearch(query: String, moodTags: String, eventTags: String): ArrayList<ListItemBean> {
        val searchList: ArrayList<ListItemBean> = ArrayList()

        val callbackListener = object : HttpRequest.CallbackListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(responseJson: JSONObject) {
                val items = responseJson.getJSONArray("data")

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

                Log.i("searchResult", searchList.toString())

                activity?.runOnUiThread {
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                Log.i("HTTPResponse", errorMessage)
                print(errorMessage)
            }
        }

        val queryString = URLEncoder.encode(query, "UTF-8")
        val moodTagsString = URLEncoder.encode(moodTags, "UTF-8")
        val eventTagsString = URLEncoder.encode(eventTags, "UTF-8")
        val httpRequest = HttpRequest()
        httpRequest.post(
            Contants.SERVER_ADDRESS +
                "/api/record/records?search=$queryString&labelList=${moodTagsString}&typeList=${eventTagsString}", RequestBody.create(null, byteArrayOf()),
            "satoken", AccountViewModel.token!!, callbackListener)

        return searchList
    }
}