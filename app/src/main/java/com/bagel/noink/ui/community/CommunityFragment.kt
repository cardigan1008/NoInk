package com.bagel.noink.ui.community

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.CommunityAdapter
import com.bagel.noink.bean.CommentItemBean
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.databinding.FragmentCommunityBinding
import com.bagel.noink.utils.CommunityHttpRequest
import com.bagel.noink.utils.HttpRequest
import org.json.JSONObject

class CommunityFragment : Fragment() {
    private val TAG = "CommunityFragment"
    private var communityList:MutableList<CommunityItemBean> = mutableListOf()

    private var _binding: FragmentCommunityBinding? = null // View Binding 的实例
    private val binding get() = _binding!! // 非空的 View Binding 引用

    private lateinit var adapter: CommunityAdapter

    private lateinit var postRecyclerView: RecyclerView
    private lateinit var communityHttpRequest: CommunityHttpRequest;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val view = binding.root

        // 初始化
        communityHttpRequest = CommunityHttpRequest()

        binding.backButton.setOnClickListener { activity?.finish() }



        getCommunityContent()
        // 找到 RecyclerView
        postRecyclerView = binding.postRecyclerView

        // 添加示例数据，您需要根据您的数据类型提供正确的数据

        // 创建并设置布局管理器
        val layoutManager = LinearLayoutManager(context)
        postRecyclerView.layoutManager = layoutManager

        // 创建并设置适配器
        adapter = CommunityAdapter(communityList)


        postRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 在 Fragment 销毁时解除绑定，避免潜在的内存泄漏
    }

    fun createFakeCommunityItemList(): List<CommunityItemBean> {
        val fakeItemList = mutableListOf<CommunityItemBean>()

        // 添加假数据
        val avatarUri = Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg") // 替换为实际的 Uri
        val imageUris = listOf(
            Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg"), // 替换为实际的 Uri
            Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg")  // 替换为实际的 Uri
        )
//        val item1 = CommunityItemBean(avatarUri, imageUris, "User1", "Content 1", 10, 5)
//        val item2 = CommunityItemBean(avatarUri, null, "User2", "Content 2", 20, 8)
//        val item3 = CommunityItemBean(avatarUri, imageUris, "User3", "Content 3", 15, 12)
//
//        // 将假数据添加到列表中
//        fakeItemList.add(item1)
//        fakeItemList.add(item2)
//        fakeItemList.add(item3)

        return fakeItemList
    }
    private fun getCommunityContent() {
        communityHttpRequest.getCommunityList(object : CommunityHttpRequest.CommunityCallbackListener {
            override fun onSuccess(responseJson: JSONObject) {
                val code = responseJson.optInt("code", -1)
                val desc = responseJson.optString("desc", "")
                val dataArray = responseJson.optJSONArray("data")

                if (code == 200 && dataArray != null) {

                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.optJSONObject(i)

                        val communityItem = createCommunityItem(dataObject)
                        communityItem?.let { communityList.add(it) }
                    }
                } else {
                    val errorMessage = "Failed to fetch community list"
                    Log.e(TAG, errorMessage)
                }

                activity?.runOnUiThread {
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                Log.e(TAG, errorMessage)
            }
        })
    }

    private fun createCommunityItem(dataObject: JSONObject?): CommunityItemBean? {
        dataObject ?: return null

        val aid = dataObject.optInt("aid", 0)
        val title = dataObject.optString("title", "")
        // temp avatar
        val avatar = Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg")
        val createdAt = dataObject.optString("createdAt", "")
        val updatedAt = dataObject.optString("updatedAt", "")
        val content = dataObject.optString("content", "")
        val imageUrl = stringToUriList(dataObject.optString("imageUrl", ""))
        val moods = dataObject.optString("moods", "")
        val events = dataObject.optString("events", "")
        val pv = dataObject.optInt("pv", 0)
        val likes = dataObject.optInt("likes", 0)
        val comments = dataObject.optInt("comments", 0)
        val state = dataObject.optInt("state", 0)
        val uid = dataObject.optInt("uid", 0)

        val communityItem = CommunityItemBean(
            aid, title, avatar, createdAt, updatedAt, content, imageUrl,
            moods, events, pv, likes, state, comments, uid, "", listOf()
        )

        val commentsArray = dataObject.optJSONArray("commentList")
        if (commentsArray != null) {
            val commentList = mutableListOf<CommentItemBean>()

            for (j in 0 until commentsArray.length()) {
                val commentObject = commentsArray.optJSONObject(j)
                val cid = commentObject?.optInt("cid", 0) ?: 0
                val pid = commentObject?.optInt("pid", 0) ?: 0
                val createAt = commentObject?.optString("createAt", "") ?: ""
                val updateAt = commentObject?.optString("updateAt", "") ?: ""
                val commentContent = commentObject?.optString("content", "") ?: ""
                val commentAid = commentObject?.optInt("aid", 0) ?: 0
                val commentState = commentObject?.optInt("state", 0) ?: 0
                val commentUser = commentObject?.optInt("commentUser", 0) ?: 0
                val username = commentObject?.optString("username", "") ?: ""
                val likes = commentObject?.optInt("likes", 0) ?: 0

                val childCommentListObject = commentObject?.optJSONArray("commentList")
                val childCommentList = mutableListOf<CommentItemBean>()

                if(childCommentListObject != null){
                    val childCommentObject = childCommentListObject.optJSONObject(j)
                    val childCid = childCommentObject?.optInt("cid", 0) ?: 0
                    val childPid = childCommentObject?.optInt("pid", 0) ?: 0
                    val childCreateAt = childCommentObject?.optString("createAt", "") ?: ""
                    val childUpdateAt = childCommentObject?.optString("updateAt", "") ?: ""
                    val childCommentContent = childCommentObject?.optString("content", "") ?: ""
                    val childCommentAid = childCommentObject?.optInt("aid", 0) ?: 0
                    val childCommentState = childCommentObject?.optInt("state", 0) ?: 0
                    val childCommentUser = childCommentObject?.optInt("commentUser", 0) ?: 0
                    val childUsername = childCommentObject?.optString("username", "") ?: ""
                    val childLikes = childCommentObject?.optInt("likes", 0) ?: 0
                    val childCommentItem = CommentItemBean(
                        childCid, childPid, childCreateAt, childUpdateAt, childCommentContent,
                        childCommentAid, childCommentState, childCommentUser, childUsername, childLikes, null
                    )
                    childCommentList.add(childCommentItem)
                }

                val commentItem = CommentItemBean(
                    cid, pid, createAt, updateAt, commentContent,
                    commentAid, commentState, commentUser, username, likes, childCommentList
                )
                commentList.add(commentItem)
            }
            communityItem.commentList = commentList
        }

        return communityItem
    }



    fun stringToUriList(inputString: String): List<Uri> {
        val uriList = mutableListOf<Uri>()
        val parts = inputString.split(",")

        for (part in parts) {
            val trimmedPart = part.trim()
            if (trimmedPart.isNotEmpty()) {
                try {
                    val uri = Uri.parse(trimmedPart)
                    uriList.add(uri)
                } catch (e: Exception) {
                    // 捕获异常，如果字符串不符合 URI 格式，跳过该部分
                    e.printStackTrace()
                }
            }
        }

        return uriList
    }
}
