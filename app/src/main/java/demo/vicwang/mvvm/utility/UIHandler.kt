package demo.vicwang.mvvm.utility

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

abstract class UIHandler<T>(reference: WeakReference<T>) : Handler() {

    private val mReference: WeakReference<T> = reference

    override fun handleMessage(msg: Message) {
        if (mReference.get() == null)
            return
        handleMessage(mReference.get(), msg)
    }

    protected abstract fun handleMessage(reference: T?, msg: Message)

    fun sendMsg(obj: Any?, what: Int) {
        val msg = this.obtainMessage()
        msg.what = what
        msg.obj = obj
        this.sendMessage(msg)
    }

    fun sendMsgDelay(obj: Any?, what: Int, delayCount: Long) {
        val msg = this.obtainMessage()
        msg.what = what
        msg.obj = obj
        this.sendMessageDelayed(msg, delayCount)
    }

}