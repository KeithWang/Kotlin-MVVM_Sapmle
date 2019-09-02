package demo.vicwang.mvvm.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import demo.vicwang.mvvm.R
import java.util.*

abstract class BasicActivity : AppCompatActivity() {
    private var mToast: Toast? = null

    private lateinit var mDialog: Dialog

    lateinit var mContext: Context

    private var mFastBackReturn: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    /*
     * Back Pressed fun
     * */
    override fun onBackPressed() {
        if (Math.abs(System.currentTimeMillis() - mFastBackReturn) < 500) {
            return
        }
        mFastBackReturn = System.currentTimeMillis()
    }

    /*
     * Normal Toast fun
     * */
    fun showToast(str: String, isLONG: Boolean) {
        if (mToast != null)
            mToast!!.cancel()
        mToast = Toast.makeText(mContext, str, if (isLONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        mToast!!.show()
    }

    /*
     * Custom error dialog
     * */
    fun openErrorDialog(contentStr: String, errorType: Int) {
        mDialog = Dialog(mContext, R.style.PauseDialog)
        mDialog.setContentView(R.layout.cusomt_dailog)
        Objects.requireNonNull<Window>(mDialog.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setCancelable(false)

        val confirm = mDialog.findViewById<Button>(R.id.custom_dialog_btn_confirm)
        val txt = mDialog.findViewById<TextView>(R.id.custom_dialog_txt_content)

        txt.text = contentStr

        val permission = View.OnClickListener { v ->
            when (v.id) {
                R.id.custom_dialog_btn_confirm -> {
                    mDialog.dismiss()
                    if (errorType == 1 || errorType == 2)
                        finish()
                }
            }
        }
        confirm.setOnClickListener(permission)

        if (!this.isFinishing)
            mDialog.show()
    }
}
