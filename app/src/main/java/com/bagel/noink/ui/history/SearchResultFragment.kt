package com.bagel.noink.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.SearchAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment(R.layout.fragment_search_result) {

    private var _binding: FragmentSearchResultBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SearchAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // 伴生对象，包含静态方法
    companion object {
        // 静态方法，用于创建 SearchResultFragment 实例，并传递数据
        fun newInstance(searchList: ArrayList<ListItemBean>): SearchResultFragment {
            val fragment = SearchResultFragment()

            // 使用 Bundle 传递数据
            val bundle = Bundle()
            bundle.putSerializable("searchList", searchList)
            fragment.arguments = bundle

            Log.i("searchFragment", "newInstance: ${searchList.size}")

            return fragment
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val root: View = binding.root
        addRecycleView(root, arguments?.getSerializable("searchList") as ArrayList<ListItemBean>)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecycleView(view: View, searchList: ArrayList<ListItemBean>) {
        recyclerView = view.findViewById(R.id.search_recycle)
        adapter = SearchAdapter(searchList)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}