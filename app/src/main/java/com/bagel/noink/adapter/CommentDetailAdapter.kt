import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bagel.noink.R
import com.bagel.noink.bean.CommentItemBean
import com.bagel.noink.ui.community.CommentViewModel
import com.bagel.noink.viewholder.CommentViewHolder
import com.google.android.material.textfield.TextInputEditText


class CommentDetailAdapter(private val commentList: List<CommentItemBean>, private val editText: TextInputEditText) :
    RecyclerView.Adapter<CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.e("114514", "get in the comment detail adapter")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        Log.e("114514", "get in the comment detail adapter")
        val comment = commentList[position]

        holder.usernameTextView.text = comment.username
        holder.commentTextView.text = comment.content
        // 设置其他评论的信息

        // 设置子评论列表的适配器和数据
        val childCommentAdapter = ChildCommentAdapter(comment.commentList ?: emptyList(), editText)
        holder.childRecyclerView.adapter = childCommentAdapter
        // 设置 RecyclerView 的布局管理器，可以根据需要设置
        holder.childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
        childCommentAdapter.notifyDataSetChanged()

        val textView = holder.itemView.findViewById<TextView>(R.id.commentTextView)
        textView.setOnClickListener {
            CommentViewModel.updatePid(comment.cid)
            editText.requestFocus()
        }
    }



    override fun getItemCount(): Int {
        return commentList.size
    }



    // 子评论的适配器
    inner class ChildCommentAdapter(private val childCommentList: List<CommentItemBean>, private val editText: TextInputEditText) :
        RecyclerView.Adapter<ChildCommentAdapter.ChildCommentViewHolder>() {
        // 内部 ViewHolder 类
        inner class ChildCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
            val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCommentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_comment_child, parent, false)
            return ChildCommentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChildCommentViewHolder, position: Int) {
            val comment = childCommentList[position]

            holder.usernameTextView.text = comment.username
            holder.commentTextView.text = comment.content

            val textView = holder.itemView.findViewById<TextView>(R.id.commentTextView)
            textView.setOnClickListener {
                CommentViewModel.updatePid(comment.cid)
                editText.requestFocus()
            }
            // 设置其他子评论的信息

            // 如果需要更多子评论的信息，可以在此处设置对应的 TextView
        }

        override fun getItemCount(): Int {
            return childCommentList.size
        }
    }



}
