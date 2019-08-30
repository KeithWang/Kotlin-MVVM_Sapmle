package demo.vicwang.mvvm.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import demo.vicwang.mvvm.R
import demo.vicwang.mvvm.activity.MainView
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem

class AnimalInfoFragment : Fragment() {

    companion object {
        const val FRAGMENT_ANIMAL_INFO_TAG = "FRAGMENT_ANIMAL_INFO_TAG"

        const val GET_ANIMAL_ITEM_KEY = "GET_ANIMAL_ITEM_KEY"

        fun newInstance(item: HouseListAnimalItem): AnimalInfoFragment {
            val fragment = AnimalInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(GET_ANIMAL_ITEM_KEY, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mView: View
    private lateinit var mCallback: MainView

    private lateinit var wImg_Header_Img: ImageView
    private lateinit var wTxt_Ch_Name: TextView
    private lateinit var wTxt_En_Name: TextView
    private lateinit var wTxt_Feature: TextView
    private lateinit var wTxt_Habitat: TextView
    private lateinit var wTxt_Diet: TextView
    private lateinit var wTxt_Update_Time: TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCallback = (context as MainView?)!!
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement ")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_animal_info, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setViewValue()
    }

    private fun initView() {
        wImg_Header_Img = mView.findViewById(R.id.animal_info_img_header_img)
        wTxt_En_Name = mView.findViewById(R.id.animal_info_txt_en_name)
        wTxt_Ch_Name = mView.findViewById(R.id.animal_info_txt_ch_name)
        wTxt_Feature = mView.findViewById(R.id.animal_info_txt_feature)
        wTxt_Habitat = mView.findViewById(R.id.animal_info_txt_habitat)
        wTxt_Diet = mView.findViewById(R.id.animal_info_txt_diet)
        wTxt_Update_Time = mView.findViewById(R.id.animal_info_txt_update_time)
    }

    private fun setViewValue() {
        if (arguments != null) {
            val mAnimalItem = arguments!!.getSerializable(GET_ANIMAL_ITEM_KEY) as HouseListAnimalItem

            val imgUrl = if (mAnimalItem.A_Pic01_URL == "") mAnimalItem.A_Pic02_URL else mAnimalItem.A_Pic01_URL
            Glide.with(activity)
                    .load(imgUrl)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                            if (activity != null)
                                wImg_Header_Img.background = activity!!.getDrawable(android.R.drawable.ic_notification_clear_all)
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(wImg_Header_Img)
            wTxt_En_Name.text = (if (mAnimalItem.A_Name_En == "") getString(R.string.no_data_show) else mAnimalItem.A_Name_En)
            wTxt_Ch_Name.text = (if (mAnimalItem.A_Name_Ch == "") getString(R.string.no_data_show) else mAnimalItem.A_Name_Ch)
            wTxt_Feature.text = (if (mAnimalItem.A_Feature == "") getString(R.string.no_data_show) else mAnimalItem.A_Feature)
            wTxt_Habitat.text = (if (mAnimalItem.A_Habitat == "") getString(R.string.no_data_show) else mAnimalItem.A_Habitat)
            wTxt_Diet.text = (if (mAnimalItem.A_Diet == "") getString(R.string.no_data_show) else mAnimalItem.A_Diet)
            wTxt_Update_Time.text = (if (mAnimalItem.A_Update == "") getString(R.string.no_data_show) else mAnimalItem.A_Update)
        } else {
            mCallback.onCallToast("Item Error", true)
            mCallback.onBackFragment()
        }
    }
}