package demo.vicwang.mvvm.utility.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.RelativeLayout

class SlideRightRelativeLayout : RelativeLayout {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    private var xFraction = 0f

    private var preDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    fun setXFraction(fraction: Float) {

        this.xFraction = fraction

        if (height == 0) {
            if (preDrawListener == null) {
                preDrawListener = ViewTreeObserver.OnPreDrawListener {
                    viewTreeObserver.removeOnPreDrawListener(preDrawListener)
                    setXFraction(xFraction)
                    true
                }
                viewTreeObserver.addOnPreDrawListener(preDrawListener)
            }
            return
        }

        val translationX = width * fraction
        setTranslationX(translationX)
    }

    fun getXFraction(): Float {
        return this.xFraction
    }
}