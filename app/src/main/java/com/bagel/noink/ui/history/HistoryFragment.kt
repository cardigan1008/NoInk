package com.bagel.noink.ui.history

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.HistoryAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.databinding.FragmentHistoryBinding
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.HttpRequest
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: HistoryAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindingUserInfo()
        addRecycleView(root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecycleView(view: View) {
        recyclerView = view.findViewById(R.id.history_recycle)
        adapter = HistoryAdapter(getHistory(), findNavController())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun getHistory(): ArrayList<ListItemBean> {
        val historyList = ArrayList<ListItemBean>()

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

                    historyList.add(
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

                // 在主线程上调用 notifyDataSetChanged()
                activity?.runOnUiThread {
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                print(errorMessage)
            }
        }

        val httpRequest = HttpRequest()
        if (AccountViewModel.token == "" || AccountViewModel.token == null || AccountViewModel.token == "null") {
            val sharedPreferences =
                activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            AccountViewModel.token = sharedPreferences?.getString("token", "")
        }
        httpRequest.get(Contants.SERVER_ADDRESS + "/api/record/allRecord", "satoken", AccountViewModel.token!!,callbackListener)

        return historyList
    }

    private fun bindingUserInfo() {
        val callbackListener = object : HttpRequest.CallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                val data = responseJson.getJSONObject("data")
                val avatar= Uri.parse(data.getString("userprofile"))
                val username = data.getString("username")

                activity?.runOnUiThread {
                    Glide.with(this@HistoryFragment)
                        .load(avatar)
                        .into(binding.avatar)
                    binding.username.text = username
                }
            }

            override fun onFailure(errorMessage: String) {
                print(errorMessage)
            }
        }

        if (AccountViewModel.token == "" || AccountViewModel.token == null || AccountViewModel.token == "null") {
            val sharedPreferences =
                activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            AccountViewModel.token = sharedPreferences?.getString("token", "")
        }

        val httpRequest = HttpRequest()
        httpRequest.get(Contants.SERVER_ADDRESS + "/api/user/userInfo", "satoken", AccountViewModel.token!!, callbackListener)
    }
}
