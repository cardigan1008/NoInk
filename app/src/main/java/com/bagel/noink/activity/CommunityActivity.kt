package com.bagel.noink.activity
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.adapter.CommunityAdapter
import com.bagel.noink.bean.CommunityItemBean
import com.bagel.noink.databinding.ActivityCommunityBinding


class CommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityBinding
    private lateinit var postRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        // 找到 RecyclerView
        postRecyclerView = findViewById(R.id.postRecyclerView)

        // 添加示例数据，您需要根据您的数据类型提供正确的数据

        // 创建并设置布局管理器
        val layoutManager = LinearLayoutManager(this)
        postRecyclerView.layoutManager = layoutManager

        // 创建并设置适配器
        val adapter = CommunityAdapter(createFakeCommunityItemList())
        postRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    // 创建一个返回 CommunityItemBean 列表的函数
    fun createFakeCommunityItemList(): List<CommunityItemBean> {
        val fakeItemList = mutableListOf<CommunityItemBean>()

        // 添加假数据
        val avatarUri = Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg") // 替换为实际的 Uri
        val imageUris = listOf(
            Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg"), // 替换为实际的 Uri
            Uri.parse("https://i.postimg.cc/cJW9nd6s/image.jpg")  // 替换为实际的 Uri
        )
        val item1 = CommunityItemBean(avatarUri, imageUris, "User1", "Content 1", 10, 5)
        val item2 = CommunityItemBean(avatarUri, null, "User2", "Content 2", 20, 8)
        val item3 = CommunityItemBean(avatarUri, imageUris, "User3", "Content 3", 15, 12)

        // 将假数据添加到列表中
        fakeItemList.add(item1)
        fakeItemList.add(item2)
        fakeItemList.add(item3)

        return fakeItemList
    }
}
