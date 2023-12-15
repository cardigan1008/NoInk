package com.bagel.noink.ui.community

import androidx.lifecycle.ViewModel
import com.bagel.noink.bean.CommentItemBean

class CommentViewModel: ViewModel() {
    companion object {
        var pid: Int = -1
        var commentItemBean: CommentItemBean? = null
        fun updatePid(pid: Int){
            this.pid = pid
        }
        fun updateCommentItemBean(commentItemBean: CommentItemBean){
            this.commentItemBean = commentItemBean
        }
    }


}