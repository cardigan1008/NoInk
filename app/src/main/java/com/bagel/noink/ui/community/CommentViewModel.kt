package com.bagel.noink.ui.community

import androidx.lifecycle.ViewModel

class CommentViewModel: ViewModel() {
    companion object {
        var pid: Int = 0

        fun updatePid(pid: Int){
            this.pid = pid
        }
    }


}