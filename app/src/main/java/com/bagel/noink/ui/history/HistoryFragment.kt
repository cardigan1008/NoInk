package com.bagel.noink.ui.history

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.HistoryAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentHistoryBinding

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
        // TODO: use backend api
        var fakeList = ArrayList<ListItemBean>()

        for (i in 0 until 10) {
            fakeList.add(ListItemBean(i, "this is a title", "this is a new text", Uri.parse("fake")))
        }

        return fakeList
    }
}
