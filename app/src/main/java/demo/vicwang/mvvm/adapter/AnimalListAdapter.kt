package demo.vicwang.mvvm.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
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
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import demo.vicwang.mvvm.utility.ViewClick
import java.util.*

class AnimalListAdapter(context: Context, data: MainHouseListItem, animalData: ArrayList<HouseListAnimalItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_TOP = 0
    private val TYPE_PLANT = 1

    private val mTopAreaData: MainHouseListItem = data
    private val mAnimalData: ArrayList<HouseListAnimalItem> = animalData
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    private var mClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerView.ViewHolder {
        if (type == TYPE_TOP) {
            val view = mInflater.inflate(R.layout.house_list_top_item, viewGroup, false)
            return ViewHolderTop(view)
        } else {
            val view = mInflater.inflate(R.layout.house_list_plant_item, viewGroup, false)
            return ViewHolderAnimal(view)
        }
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_TOP) {
            /*
             * Top Area
             * */
            Glide.with(mContext)
                    .load(mTopAreaData.E_Pic_URL)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((viewHolder as ViewHolderTop).wImg_Top_Pic)
            viewHolder.wTxt_Top_Info.text = mTopAreaData.E_Info
            viewHolder.wTxt_Top_Rest.text = if (mTopAreaData.E_Memo == "") mContext.getString(R.string.main_house_no_rest_date) else mTopAreaData.E_Memo
            viewHolder.wTxt_Top_Area.text = mTopAreaData.E_Category

            val link = "<a href=\"" + mTopAreaData.E_URL + "\">" + mContext.getString(R.string.house_list_item_top_open_web) + "</a>"
            var str: Spanned
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                str = Html.fromHtml(link, Html.FROM_HTML_MODE_LEGACY)
            } else {
                str = Html.fromHtml(link)
            }
            viewHolder.wTxt_Top_Open_Web.text = str
            viewHolder.wTxt_Top_Open_Web.movementMethod = LinkMovementMethod.getInstance()

            viewHolder.wTxt_Top_No_Plant.visibility = if (mAnimalData.size == 0) View.VISIBLE else View.GONE

        } else {

            /*
             * Animal data Area
             * */

            val imgUrl = if (mAnimalData.get(position - 1).A_Pic01_URL == "") mAnimalData.get(position - 1).A_Pic02_URL else mAnimalData.get(position - 1).A_Pic01_URL;
            Glide.with(mContext)
                    .load(imgUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                            (viewHolder as ViewHolderAnimal).wProcessBar.visibility = View.GONE
                            viewHolder.wImg_Animal_Pic.background = mContext.getDrawable(android.R.drawable.ic_notification_clear_all)
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            (viewHolder as ViewHolderAnimal).wProcessBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into((viewHolder as ViewHolderAnimal).wImg_Animal_Pic)

            viewHolder.wTxt_Name.text = mAnimalData.get(position - 1).A_Name_Ch
            viewHolder.wTxt_AlsoKnow.text = if (mAnimalData.get(position - 1).A_Feature.equals("")) mContext.getString(R.string.house_list_item_no_feature) else mAnimalData.get(position - 1).A_Feature

        }


    }

    override fun getItemCount(): Int {
        return mAnimalData.size + 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            TYPE_TOP
        else
            TYPE_PLANT
    }

    inner class ViewHolderTop internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var wImg_Top_Pic: ImageView
        internal var wTxt_Top_Info: TextView
        internal var wTxt_Top_Rest: TextView
        internal var wTxt_Top_Area: TextView
        internal var wTxt_Top_Open_Web: TextView
        internal var wTxt_Top_No_Plant: TextView

        init {
            wImg_Top_Pic = itemView.findViewById(R.id.house_list_item_top_img_house)
            wTxt_Top_Info = itemView.findViewById(R.id.house_list_item_top_txt_house_info)
            wTxt_Top_Rest = itemView.findViewById(R.id.house_list_item_top_txt_house_rest_time)
            wTxt_Top_Area = itemView.findViewById(R.id.house_list_item_top_txt_house_area)
            wTxt_Top_Open_Web = itemView.findViewById(R.id.house_list_item_top_txt_house_open_web)
            wTxt_Top_No_Plant = itemView.findViewById(R.id.house_list_item_top_txt_no_plant_show)

        }

    }

    inner class ViewHolderAnimal internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var wImg_Animal_Pic: ImageView
        internal var wTxt_Name: TextView
        internal var wTxt_AlsoKnow: TextView
        internal var wProcessBar: ProgressBar
        internal var wLay_Click_Area: RelativeLayout

        private val nCustomClickListener = object : ViewClick() {

            override fun CustomOnClick(view: View) {
                if (mClickListener != null) {
                    mClickListener!!.onItemClick(view, getAdapterPosition())
                }
            }
        }

        init {
            wImg_Animal_Pic = itemView.findViewById(R.id.house_list_animal_item_img_pic)
            wTxt_Name = itemView.findViewById(R.id.house_list_plant_item_txt_name)
            wTxt_AlsoKnow = itemView.findViewById(R.id.house_list_plant_item_txt_alsoknow)
            wProcessBar = itemView.findViewById(R.id.house_list_animal_item_loading_processbar)
            wLay_Click_Area = itemView.findViewById(R.id.house_list_plant_item_lay_click_area)

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
