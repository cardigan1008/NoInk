package com.bagel.noink.utils


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.service.media.MediaBrowserService.BrowserRoot
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView
import com.bagel.noink.R
import org.jetbrains.annotations.Nullable


class LoadingDialog : Dialog {
    private var objectAnimator: ObjectAnimator? = null
    private var circle: AppCompatImageView? = null
    private val duration: Long = 5000
    private lateinit var context: Context
    constructor(context: Context) : super(context){
        this.context = context
    }
    constructor(context: Context, themeResId: Int) : super(context, themeResId){
        this.context = context
    }
    protected constructor(
        context: Context,
        cancelable: Boolean,
        @Nullable cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener){
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

    }

    override fun show() {
        super.show()
        startAnim()
        setOnDismissListener { dialog: DialogInterface? -> endAnim() }
    }

    /**
     * 启动动画
     */
    private fun startAnim() {
        setCanceledOnTouchOutside(false)
        circle = (context as Activity).findViewById(R.id.loading)
        var objectAnimator = ObjectAnimator.ofFloat(circle, "rotation", 0f, 360f)
        //设置动画时间
        objectAnimator.setDuration(duration)
        //设置动画重复次数，这里-1代表无限
        objectAnimator.setRepeatCount(Animation.INFINITE)
        //设置动画循环模式。
        objectAnimator.setRepeatMode(ValueAnimator.RESTART)
        objectAnimator.start()
    }

    /**
     * 结束动画
     */
    private fun endAnim() {
        objectAnimator!!.end()
        objectAnimator = null
        circle = null
    }
}
