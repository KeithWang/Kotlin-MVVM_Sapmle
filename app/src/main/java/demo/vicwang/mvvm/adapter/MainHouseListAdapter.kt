package demo.vicwang.mvvm.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import demo.vicwang.mvvm.R
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import demo.vicwang.mvvm.utility.ViewClick
import java.util.*

class MainHouseListAdapter(context: Context, data: ArrayList<MainHouseListItem>) : RecyclerView.Adapter<MainHouseListAdapter.ViewHolder>() {


    private val mData: ArrayList<MainHouseListItem> = data
    private val mContext: Context = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: MainHouseListAdapter.ItemClickListener? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MainHouseListAdapter.ViewHolder {
        val view = mInflater.inflate(R.layout.main_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MainHouseListAdapter.ViewHolder, i: Int) {
        viewHolder.wTxt_Title.text = mData[i].E_Name
        viewHolder.wTxt_Content.text = mData[i].E_Info
        viewHolder.wTxt_Sub_Content.text = if (mData.get(i).E_Memo == "") mContext.getString(R.string.main_house_no_rest_date) else mData.get(i).E_Memo
        viewHolder.wImg_pic.background = null

        Glide.with(mContext)
                .load(mData[i].E_Pic_URL)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                        viewHolder.wProcessBar.visibility = View.GONE
                        viewHolder.wImg_pic.background = mContext.getDrawable(android.R.drawable.ic_notification_clear_all)
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        viewHolder.wProcessBar.visibility = View.GONE
                        return false
                    }
                }).into(viewHolder.wImg_pic)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var wTxt_Title: TextView
        internal var wTxt_Content: TextView
        internal var wTxt_Sub_Content: TextView
        internal var wImg_pic: ImageView
        internal var wProcessBar: ProgressBar
        internal var wLay_Click_Area: RelativeLayout

        private val nCustomClickListener = object : ViewClick() {

            override fun CustomOnClick(view: View) {
                if (mClickListener != null) {
                    mClickListener!!.onItemClick(view, adapterPosition)
                }
            }
        }

        init {
            wTxt_Title = itemView.findViewById(R.id.main_list_item_txt_title)
            wTxt_Content = itemView.findViewById(R.id.main_list_item_txt_content)
            wTxt_Sub_Content = itemView.findViewById(R.id.main_list_item_txt_sub_content)
            wImg_pic = itemView.findViewById(R.id.main_list_item_img_pic)
            wProcessBar = itemView.findViewById(R.id.main_list_item_loading_processbar)
            wLay_Click_Area = itemView.findViewById(R.id.main_list_item_lay_click_area)

            wLay_Click_Area.setOnClickListener(nCustomClickListener)
        }

    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}