package demo.vicwang.mvvm.utility

import android.view.View

abstract class ViewClick : View.OnClickListener {
    private var mFastClickReturn: Long = 0

    private fun fastClickCheck(): Boolean {
        if (System.currentTimeMillis() - mFastClickReturn < 500) {
            return true
        }
        mFastClickReturn = System.currentTimeMillis()
        return false
    }

    override fun onClick(view: View?) {
        if (fastClickCheck() || view == null)
            return
        CustomOnClick(view)
    }

    protected abstract fun CustomOnClick(view: View)

}