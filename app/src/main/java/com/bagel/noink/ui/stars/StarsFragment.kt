package com.bagel.noink.ui.stars

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.StarsAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentStarsBinding

class StarsFragment : Fragment() {

    private var _binding: FragmentStarsBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: StarsAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStarsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        addRecycleView(root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecycleView(view: View) {
        recyclerView = view.findViewById(R.id.stars_recycle)
        adapter = StarsAdapter(getStars())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun getStars(): ArrayList<ListItemBean> {
        // TODO: use backend api
        var fakeList = ArrayList<ListItemBean>()

        for (i in 0 until 10) {
            fakeList.add(ListItemBean(i, "this is a title", "this is a new text", Uri.parse("fake")))
        }

        return fakeList
    }
}