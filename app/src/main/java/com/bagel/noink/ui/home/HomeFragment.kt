package com.bagel.noink.ui.home

import RecordCardAdapter
import kotlin.random.Random
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bagel.noink.databinding.FragmentHomeBinding
import android.content.Intent
import android.database.Cursor
import android.icu.util.Calendar
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bagel.noink.R
import com.bagel.noink.adapter.HistoryAdapter
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.bean.RecordCardBean
import com.bagel.noink.databinding.FragmentHomeCatBinding
import com.bagel.noink.ui.account.AccountViewModel
import com.bagel.noink.utils.AliyunOSSManager
import com.bagel.noink.utils.Contants
import com.bagel.noink.utils.HttpRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeCatBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var navController: NavController
    private lateinit var aliyunOSSManager: AliyunOSSManager
    private var recordCardAdapter: RecordCardAdapter? = null

    companion object {
        private const val PICK_IMAGES_REQUEST_CODE = 101 // 更改请求码，以便处理多个图片选择
    }

    // 用于存储选择的多个图片的 Uri 列表
    private val selectedImageUris = mutableListOf<Uri>()

    @SuppressLint("DiscouragedApi", "SetTextI18n", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        aliyunOSSManager = AliyunOSSManager(requireContext())

        _binding = FragmentHomeCatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        navController = findNavController()

        val yearMonth: TextView = binding.yearMonth
        val textView: TextView = binding.textHome
        //val bar: View = binding.bottomBar
        val imageView: ImageView = binding.imageView
        val button: Button = binding.uploadButton

        // 计算今天的日期
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        yearMonth.text = "${year}年${month}月${day}日"

        // Set text from ViewModel
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        val imageResource = resources.getIdentifier("ic_button_ink_bottle", "mipmap", requireActivity().packageName)
//        if (imageResource != 0) { // 检查资源是否存在
//            val imageView = ImageView(requireContext())
//            val params = GridLayout.LayoutParams()
//            params.width = 200 // 设置图片宽度
//            params.height = 200 // 设置图片高度
//            imageView.layoutParams = params
//
//            // 设置 ImageView 的图片资源
//            imageView.setImageResource(imageResource)
//
//        } else {
//            // 如果资源不存在，进行相应的处理，如打印日志或其他操作
//            Log.e("ImageView", "PNG image resource not found")
//        }

        button.setOnClickListener {
            navController.navigate(R.id.nav_mood)

//            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
//            galleryIntent.type = "image/*"
//            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 允许多选图片
//            startActivityForResult(galleryIntent, PICK_IMAGES_REQUEST_CODE)
        }

        val viewPager: ViewPager2 = binding.viewPager
        val emptyList: List<RecordCardBean> = listOf()
        recordCardAdapter = RecordCardAdapter(emptyList, navController) // cardsList 是包含卡片数据的列表
        viewPager.adapter = recordCardAdapter
        recordCardAdapter!!.updateData(getCardList())
        return root
    }

    fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') // 允许的字符集合
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            // 清空已选图片列表，以便重新添加选择的图片
            selectedImageUris.clear()

            // 获取从系统图片选择器返回的所有 Uri
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        requireActivity().contentResolver.query(uri, projection, null, null, null)
                    var filePath: String
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            filePath = it.getString(columnIndex)
                            // filePath 变量包含实际的本地文件路径
//                            val aliyunOSSUrl = aliyunOSSManager.uploadImage(filePath,"test"+ generateRandomString(6));
                            val aliyunOSSUrl =
                                "https://cardigan1008.oss-cn-hangzhou.aliyuncs.com/test"
                            if (aliyunOSSUrl != null) {
                                selectedImageUris.add(Uri.parse(aliyunOSSUrl))
                            }
                            // 将选择的图片 Uri 添加到列表中
                        }
                    }
                }
            } else {
                // 单选图片时处理
                val uri = data?.data!!
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    requireActivity().contentResolver.query(uri, projection, null, null, null)
                var filePath: String
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        filePath = it.getString(columnIndex)
                        // filePath 变量包含实际的本地文件路径

//                        val aliyunOSSUrl = aliyunOSSManager.uploadImage(filePath,"test");
                        val aliyunOSSUrl = "https://cardigan1008.oss-cn-hangzhou.aliyuncs.com/test"
                        if (aliyunOSSUrl != null) {
                            selectedImageUris.add(Uri.parse(aliyunOSSUrl))
                        }
                        // 将选择的图片 Uri 添加到列表中
                    }
                }
            }

            val textGenerationFragment = TextGenerationFragment.newInstance(selectedImageUris)
            navController.navigate(R.id.nav_textGen, textGenerationFragment.arguments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getCardList(): MutableList<RecordCardBean> {

        val uriDemo = Uri.parse("https://quasdo.oss-cn-hangzhou.aliyuncs.com/img/YFLTFY_JPDKCQFOZ2LBZYPQ.png")

        val data1 = RecordCardBean(
            1,
            "2023年12月11日",
            "考研加油",
            uriDemo,
            "离考研只剩两个月了，最后冲刺一把",
            listOf(uriDemo)
        )

        val cardList: MutableList<RecordCardBean> = ArrayList()
        // 第一张卡片无需使用真实数据
        cardList.add(data1)

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
                    val year = dateString.substring(0, 4)
                    val month = dateString.substring(5, 7)
                    val day = dateString.substring(8, 10)

                    cardList.add(
                        RecordCardBean(
                            item.getInt("rid"),
                            year + "年" + month + "月" + day + "日",
                            item.getString("title"),
                            uriList[0],
                            item.getString("generatedText"),
                            uriList
                        )
                    )
                }

                // 在主线程上调用 notifyDataSetChanged()
                activity?.runOnUiThread {
                    recordCardAdapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(errorMessage: String) {
                print(errorMessage)
            }
        }

        val httpRequest = HttpRequest()
        httpRequest.get(
            Contants.SERVER_ADDRESS + "/api/record/allRecord",
            "satoken",
            AccountViewModel.token!!,
            callbackListener
        )
        return cardList
    }
}

