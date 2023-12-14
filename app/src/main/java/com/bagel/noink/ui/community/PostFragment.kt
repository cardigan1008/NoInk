package com.bagel.noink.ui.community

import CommentDetailAdapter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bagel.noink.R
import com.bagel.noink.bean.CommentItemBean
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.databinding.FragmentPostBinding
import com.bagel.noink.utils.CommunityHttpRequest
import com.bumptech.glide.Glide
import org.json.JSONObject

class PostFragment : Fragment(R.layout.fragment_post) {
    private val TAG: String = "PostFragment"

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val communityHttpRequest: CommunityHttpRequest = CommunityHttpRequest()

    private lateinit var communityItemBean: CommunityItemBean
    private lateinit var commentDetailAdapter: CommentDetailAdapter
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val receivedAid = arguments?.getString("aid") ?: "" // 获取传递的 aid 数据

        communityHttpRequest.getCommunityDetail(receivedAid, object : CommunityHttpRequest.CommunityCallbackListener{
            override fun onSuccess(responseJson: JSONObject) {
                communityItemBean = createCommunityItem(responseJson) ?: return
                val imageUris: List<Uri>? = communityItemBean.imageUrls
                val viewPager = binding.viewPager
                val pagerIndicator = binding.pagerIndicator
                imagePagerAdapter = imageUris?.let { ImagePagerAdapter(it) }!!
                viewPager.adapter = imagePagerAdapter
                pagerIndicator.setViewPager(viewPager)

                val comments: List<CommentItemBean>? = communityItemBean.commentList
                commentDetailAdapter = comments?.let { CommentDetailAdapter(it) }!!
                binding.commentRecyclerView.adapter = commentDetailAdapter

                activity?.runOnUiThread{
                    commentDetailAdapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                Log.e(TAG, "Failed to fetch community detail: $errorMessage")
                // Handle failure appropriately
            }
        })

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun createCommunityItem(dataObjectOrigin: JSONObject?): CommunityItemBean? {
        dataObjectOrigin ?: return null

        var dataObject = dataObjectOrigin.optJSONObject("data")!!
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
        val username = dataObject.optString("username","")

        val communityItem = CommunityItemBean(
            aid, title, avatar, createdAt, updatedAt, content, imageUrl,
            moods, events, pv, likes, state, comments, uid, username, listOf()
        )

        val commentsArray = dataObject.optJSONArray("commentList")
        if (commentsArray != null) {
            val commentList = mutableListOf<CommentItemBean>()

            for (j in 0 until commentsArray.length()) {
                val commentObject = commentsArray.optJSONObject(j)
                val commentItem = createCommentItem(commentObject)
                commentItem?.let { commentList.add(it) }
            }
            communityItem.commentList = commentList
        }

        return communityItem
    }

    private fun createCommentItem(commentObject: JSONObject?): CommentItemBean? {
        commentObject ?: return null

        val cid = commentObject.optInt("cid", 0)
        val pid = commentObject.optInt("pid", 0)
        val createAt = commentObject.optString("createAt", "")
        val updateAt = commentObject.optString("updateAt", "")
        val content = commentObject.optString("content", "")
        val aid = commentObject.optInt("aid", 0)
        val state = commentObject.optInt("state", 0)
        val commentUser = commentObject.optInt("commentUser", 0)
        val username = commentObject.optString("username", "")
        val likes = commentObject.optInt("likes", 0)
        val avatar = Uri.parse(commentObject.optString("userprofile", ""))

        val commentItem = CommentItemBean(
            cid, pid, createAt, updateAt, content,
            aid, state, commentUser, username, likes, null, avatar
        )

        val childCommentListObject = commentObject.optJSONArray("commentList")
        if (childCommentListObject != null) {
            val childCommentList = mutableListOf<CommentItemBean>()

            for (k in 0 until childCommentListObject.length()) {
                val childCommentObject = childCommentListObject.optJSONObject(k)
                val childCommentItem = createCommentItem(childCommentObject)
                childCommentItem?.let { childCommentList.add(it) }
            }
            commentItem.commentList = childCommentList
        }

        return commentItem
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

    private inner class ImagePagerAdapter(private val imageURIs: List<Uri>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            Glide.with(container)
                .load(imageURIs[position])
                .into(imageView)
            container.addView(imageView)
            return imageView
        }

        override fun getCount(): Int {
            return imageURIs.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
