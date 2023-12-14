import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.activity.DetailsActivity
import com.bagel.noink.bean.ListItemBean
import com.bagel.noink.bean.RecordCardBean
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class RecordCardAdapter(private var cards: List<ListItemBean>, private val navController: NavController, private val onItemClick: (position: Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_CARD_NEW = 1
        private const val VIEW_TYPE_CARD_RECORD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_CARD_NEW
        } else {
            VIEW_TYPE_CARD_RECORD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CARD_NEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_new, parent, false)
                CardViewHolder1(view)
            }
            VIEW_TYPE_CARD_RECORD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                CardViewHolder2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = cards[position]
        when (holder) {
            is CardViewHolder1 -> {
                // 绑定 CardViewHolder1 的数据和视图
                holder.bind(card)
            }
            is CardViewHolder2 -> {
                // 绑定 CardViewHolder2 的数据和视图
                holder.bind(card, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class CardViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // CardViewHolder1 的视图组件和绑定方法
        private val yearMonth: TextView = itemView.findViewById(R.id.yearMonth)
        private val text_home: TextView = itemView.findViewById(R.id.text_home)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val uploadButton: Button = itemView.findViewById(R.id.uploadButton)
        fun bind(card: ListItemBean) {
            // 绑定 CardViewHolder1 的数据和视图

            // 计算今天的日期
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            yearMonth.text = "${year}年${month}月${day}日"

            text_home.text = "记录下今天干的事情"
            uploadButton.setOnClickListener {
                itemView.findNavController().navigate(R.id.nav_mood)
            }
        }
    }

    inner class CardViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // CardViewHolder2 的视图组件和绑定方法
        private val dataView: TextView = itemView.findViewById(R.id.yearMonth)
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val contentView: TextView = itemView.findViewById(R.id.content)

        fun bind(card: ListItemBean, position: Int) {
            // 绑定 CardViewHolder2 的数据和视图
            val format = SimpleDateFormat("yyyy年M月d日", Locale.getDefault())
            dataView.text = format.format(card.createDate)
            titleTextView.text = card.title

            // 使用 Glide 加载并绑定 Uri 到 imageView
            Glide.with(itemView.context)
                .load(card.coverUri)
                .into(imageView)
            contentView.text = card.text

            itemView.setOnClickListener {
                onItemClick(position)
                val bundle = bundleOf(
                    "listItem" to card
                )
                navController.navigate(R.id.action_nav_home_to_nav_card_details, bundle)
            }
        }
    }

    fun updateData(newCards: List<ListItemBean>) {
        cards = newCards
    }

}