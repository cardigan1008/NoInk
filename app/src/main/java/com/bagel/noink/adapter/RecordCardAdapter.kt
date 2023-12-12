import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.bean.RecordCardBean
import com.bumptech.glide.Glide

class RecordCardAdapter(private var cards: List<RecordCardBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                holder.bind(card)
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
        fun bind(card: RecordCardBean) {
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

        fun bind(card: RecordCardBean) {
            // 绑定 CardViewHolder2 的数据和视图
            dataView.text = card.date
            titleTextView.text = card.title

            // 使用 Glide 加载并绑定 Uri 到 imageView
            Glide.with(itemView.context)
                .load(card.photo)
                .into(imageView)
            contentView.text = card.content
        }
    }

    fun updateData(newCards: List<RecordCardBean>) {
        cards = newCards
    }
}