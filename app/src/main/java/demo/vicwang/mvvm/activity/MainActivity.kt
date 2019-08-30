package demo.vicwang.mvvm.activity

import android.arch.lifecycle.Observer
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import demo.vicwang.mvvm.R
import demo.vicwang.mvvm.adapter.MainHouseListAdapter
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import demo.vicwang.mvvm.fragment.AnimalInfoFragment
import demo.vicwang.mvvm.fragment.HouseInfoFragment
import demo.vicwang.mvvm.mvvm.viewmodel.DataLoadViewModel
import demo.vicwang.mvvm.mvvm.viewmodel.item.AnimalListDataResultItem
import demo.vicwang.mvvm.mvvm.viewmodel.item.DataLoadStatus
import demo.vicwang.mvvm.mvvm.viewmodel.item.HouseDataResultItem
import demo.vicwang.mvvm.utility.ViewClick
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BasicActivity(), MainView {

    /*
     * Tool or page widget
     * */
    private lateinit var wRecycleView: RecyclerView

    private lateinit var wLay_Loading_Area: FrameLayout
    /*
     * Toolbar
     * */
    private lateinit var wTxt_Toolbar_Title: TextView
    private lateinit var wImg_Toolbar_Home: ImageView
    private lateinit var wLay_Btn_Home: FrameLayout

    /*
     * Main Page data
     * */
    private var mData = ArrayList<MainHouseListItem>()

    private lateinit var mAreaSelectItem: MainHouseListItem

    private var mIsHome = true

    /*
    * ViewModel by Koin Inject
    * */
    private val mDataViewModel: DataLoadViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInit()
        setViewValue()
        setViewListener()

    }


    private fun viewInit() {

        wRecycleView = findViewById(R.id.main_recycleview)

        wTxt_Toolbar_Title = findViewById(R.id.main_toolsbar_txt_title)
        wImg_Toolbar_Home = findViewById(R.id.main_toolsbar_img_home)
        wLay_Btn_Home = findViewById(R.id.main_toolsbar_lay_btn_home_click)

        wLay_Loading_Area = findViewById(R.id.main_lay_loading_area)

    }

    private fun setViewValue() {

        setTitleBar("", true, false)

        subscriptViewModel()

        /*
        * First to Load Data
        * */
        mDataViewModel.onLoadHouseData()
    }


    private fun setViewListener() {
        wLay_Btn_Home.setOnClickListener(mNormalClickListener)
    }


    /*
    * MVVM LiveData Subscript
    * */
    private fun subscriptViewModel() {

        /*
        * Data Loading
        * */
        mDataViewModel.getLoadingStatus().observe(this, Observer<Boolean> { item ->
            onShowLoadingView(item!!)
        })

        /*
        * Show Main House Data
        * */
        mDataViewModel.getHouseData().observe(this, Observer<HouseDataResultItem> { item ->
            if (item!!.getStatus() == DataLoadStatus.SUCCESS) {
                onHouseDataLoadSuccess(item.getHouseData()!!)
            } else {
                onShowErrorMsg(item.getErrorType()!!)
            }
        })

        /*
        * Show Animal List Select Data
        * */
        mDataViewModel.getAnimalListData().observe(this, Observer<AnimalListDataResultItem> { item ->
            if (item!!.getStatus() == DataLoadStatus.SUCCESS) {
                onAnimalDataLoadSuccess(item.getHouseListAnimalData()!!, item.getHouseItem()!!)
            } else {
                onShowErrorMsg(item.getErrorType()!!)
            }
        })

    }

    /*
     * Control View Area
     * */
    private fun onShowLoadingView(isShow: Boolean) {
        if (isShow)
            wLay_Loading_Area.visibility = View.VISIBLE
        else
            wLay_Loading_Area.visibility = View.INVISIBLE
    }

    private fun onShowErrorMsg(errorType: Int) {
        onShowLoadingView(false)
        openErrorDialog(getString(R.string.http_get_error), errorType)
    }

    private fun onHouseDataLoadSuccess(o: Any) {
        onShowLoadingView(false)
        loadListItem(o as ArrayList<MainHouseListItem>)
    }

    private fun onAnimalDataLoadSuccess(o: Any, item: MainHouseListItem) {
        setTitleBar(item.E_Name, false, false)
        onShowLoadingView(false)

        onOpenFragment(
                HouseInfoFragment.newInstance(item, o as ArrayList<HouseListAnimalItem>)
                , HouseInfoFragment.FRAGMENT_HOUSE_INFO_TAG)
    }

    /*
     * Custom Title Bar
     * */
    private fun setTitleBar(title: String, menu: Boolean, ignoreAnim: Boolean) {
        mIsHome = menu
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val drawable: AnimatedVectorDrawable
        if (menu) {
            drawable = getDrawable(R.drawable.ic_back_animatable) as AnimatedVectorDrawable
        } else {
            drawable = getDrawable(R.drawable.ic_menu_animatable) as AnimatedVectorDrawable
        }
        if (!ignoreAnim)
            wImg_Toolbar_Home.setImageDrawable(drawable)
        drawable.start()

        wTxt_Toolbar_Title.text = if (title == "") getString(R.string.main_taipei_zoo) else title
    }

    /*
     * Add Fragment in main_fragment_outer_page_container
     * */
    private fun onOpenFragment(f: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.fragment_anim_slide_right_in, R.animator.fragment_anim_slide_do_nothing, R.animator.fragment_anim_slide_do_nothing, R.animator.fragment_anim_slide_right_out)
                .add(R.id.main_fragment_outer_page_container, f, fragmentTag)
                .addToBackStack(fragmentTag)
                .commit()
    }

    /*
     * Normal Click
     * */
    private val mNormalClickListener = object : ViewClick() {
        override fun CustomOnClick(view: View) {
            when (view.id) {
                R.id.main_toolsbar_lay_btn_home_click -> {
                    if (mIsHome)
                        onCallToast(":)", false)
                    else
                        onBackPressed()
                }
            }
        }
    }


    /*
     * Loading Main page List Item fun
     * */
    private fun loadListItem(data: ArrayList<MainHouseListItem>) {
        mData.clear()
        mData = data
        wRecycleView.layoutManager = LinearLayoutManager(mContext)
        val mAdapter = MainHouseListAdapter(mContext, mData)
        mAdapter.setClickListener(recycleClick)
        wRecycleView.adapter = mAdapter
        wRecycleView.itemAnimator = DefaultItemAnimator()
    }

    /*
     * Main Page list click listener
     * */
    private val recycleClick = object : MainHouseListAdapter.ItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            onShowLoadingView(true)
            mAreaSelectItem = mData[position]
            mDataViewModel.onLoadAnimalListData(mData[position])
        }
    }

    /*
     * Back Pressed fun
     * */
    override fun onBackPressed() {
        onBackFragment()
    }

    /*
     * Fragment Callback Area
     * */
    override fun onCallToast(str: String, Long: Boolean) {
        showToast(str, Long)
    }

    override fun onBackFragment() {
        val fHouse = supportFragmentManager.findFragmentByTag(HouseInfoFragment.FRAGMENT_HOUSE_INFO_TAG)
        val fAnimalInfo = supportFragmentManager.findFragmentByTag(AnimalInfoFragment.FRAGMENT_ANIMAL_INFO_TAG)
        when {
            fAnimalInfo != null -> {
                setTitleBar(mAreaSelectItem.E_Name, false, true)
                supportFragmentManager.popBackStack(AnimalInfoFragment.FRAGMENT_ANIMAL_INFO_TAG, 1)
            }
            fHouse != null -> {
                setTitleBar("", true, false)
                supportFragmentManager.popBackStack(HouseInfoFragment.FRAGMENT_HOUSE_INFO_TAG, 1)
            }
            else -> finish()
        }
    }

    override fun onOpenAnimalInfoPage(item: HouseListAnimalItem) {
        setTitleBar(item.A_Name_Ch, false, true)
        onOpenFragment(AnimalInfoFragment.newInstance(item), AnimalInfoFragment.FRAGMENT_ANIMAL_INFO_TAG)
    }
}
