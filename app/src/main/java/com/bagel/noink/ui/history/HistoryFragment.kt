package com.bagel.noink.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.HistoryAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentHistoryBinding
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.HttpRequest
import org.json.JSONObject

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: HistoryAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        addRecycleView(root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecycleView(view: View) {
        recyclerView = view.findViewById(R.id.history_recycle)
        adapter = HistoryAdapter(getHistory())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun getHistory(): ArrayList<ListItemBean> {
        val historyList = ArrayList<ListItemBean>()

        val callbackListener = object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                val items = responseJson.getJSONArray("data")

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val historyItem = ListItemBean(item.getString("id").toInt(),
                        item.getString("originText"), item.getString("generatedText"),
                        item.getString("imageUrl").toUri())
                    historyList.add(historyItem)
                }
            }

            override fun onFailure(errorMessage: String) {
                TODO("Not yet implemented")
            }
        }

        val httpRequest = HttpRequest()
        httpRequest.get(Contants.SERVER_ADDRESS + "/api/record/allRecord", callbackListener)

        return historyList
    }
}
