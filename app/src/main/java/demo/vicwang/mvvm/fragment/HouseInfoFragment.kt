package demo.vicwang.mvvm.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.vicwang.mvvm.R
import demo.vicwang.mvvm.activity.MainView
import demo.vicwang.mvvm.adapter.AnimalListAdapter
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import java.util.*

class HouseInfoFragment : Fragment() {

    companion object {
        const val FRAGMENT_HOUSE_INFO_TAG = "FRAGMENT_HOUSE_INFO_TAG"

        const val GET_PAGE_TOP_ITEM_KEY = "GET_PAGE_TOP_ITEM_KEY"

        const val GET_PAGE_ANIMAL_LIST_ITEM_KEY = "GET_PAGE_ANIMAL_LIST_ITEM_KEY"

        fun newInstance(pageItem: MainHouseListItem, animalListData: ArrayList<HouseListAnimalItem>): HouseInfoFragment {
            val fragment = HouseInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(GET_PAGE_TOP_ITEM_KEY, pageItem)
            bundle.putSerializable(GET_PAGE_ANIMAL_LIST_ITEM_KEY, animalListData)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mView: View
    private lateinit var mCallback: MainView

    private lateinit var wRecycleView: RecyclerView

    /*
     * Page data
     * */
    private var mAnimalData = ArrayList<HouseListAnimalItem>()

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
        mView = inflater.inflate(R.layout.fragment_house_info, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setViewValue()
    }

    private fun initView() {
        wRecycleView = mView.findViewById(R.id.house_main_recycleview)

    }

    private fun setViewValue() {
        if (arguments != null) {
            val mTopItem = arguments!!.getSerializable(GET_PAGE_TOP_ITEM_KEY) as MainHouseListItem
            mAnimalData = arguments!!.getSerializable(GET_PAGE_ANIMAL_LIST_ITEM_KEY) as ArrayList<HouseListAnimalItem>
            loadListItem(mTopItem, mAnimalData)
        } else {
            mCallback.onCallToast("Item Error", true)
            mCallback.onBackFragment()
        }

    }

    /*
     * Loading List Item fun
     * */
    private fun loadListItem(topData: MainHouseListItem, plantData: ArrayList<HouseListAnimalItem>) {
        wRecycleView.layoutManager = LinearLayoutManager(getContext())
        val mAdapter = AnimalListAdapter(getContext()!!, topData, plantData)
        mAdapter.setClickListener(mRecycleClick)
        wRecycleView.adapter = mAdapter
        wRecycleView.itemAnimator = DefaultItemAnimator()
    }

    /*
     * Animal list click listener
     * */
    private val mRecycleClick = object : AnimalListAdapter.ItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            Log.d("Test", mAnimalData[position - 1].A_Name_Ch)
            mCallback.onOpenAnimalInfoPage(mAnimalData[position - 1])
        }
    }
}