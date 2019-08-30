package demo.vicwang.mvvm.mvvm.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import demo.vicwang.mvvm.mvvm.model.ApiRepository
import demo.vicwang.mvvm.mvvm.model.ResponseItem
import demo.vicwang.mvvm.mvvm.rxprovider.TrampolineSchedulerProvider
import demo.vicwang.mvvm.mvvm.viewmodel.item.AnimalListDataResultItem
import demo.vicwang.mvvm.mvvm.viewmodel.item.DataLoadStatus
import demo.vicwang.mvvm.mvvm.viewmodel.item.HouseDataResultItem
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*


class DataLoadViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DataLoadViewModel

    private val rxProvider: TrampolineSchedulerProvider = TrampolineSchedulerProvider()

    @Mock
    private lateinit var apiRope: ApiRepository

    @Mock
    private lateinit var houseDataObserver: Observer<HouseDataResultItem>

    @Mock
    private lateinit var animalListDataObserver: Observer<AnimalListDataResultItem>


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this);

        viewModel = DataLoadViewModel(apiRope, rxProvider)

        viewModel.getHouseData().observeForever(houseDataObserver)

        viewModel.getAnimalListData().observeForever(animalListDataObserver)
    }

    @Test
    fun `Init House Data Has Http Failed or Observable Is Null`() {
        val expectErrorType = 1


        `when`(apiRope.getHouseData()).thenReturn(Observable.error(NullPointerException()))

        viewModel.onLoadHouseData()

        verify(houseDataObserver).onChanged(HouseDataResultItem(DataLoadStatus.FAIL, null, expectErrorType))

    }

    @Test
    fun `Init House Data Has Http Success But Json Inner Error`() {
        val successJsonStr = "{}"
        val expectErrorType = 1

        val item = Gson().fromJson(successJsonStr, ResponseItem::class.java)

        `when`(apiRope.getHouseData()).thenReturn(Observable.just(item))

        viewModel.onLoadHouseData()

        verify(houseDataObserver).onChanged(HouseDataResultItem(DataLoadStatus.FAIL, null, expectErrorType))
    }

    @Test
    fun `Init House Data Has Http Success`() {
        val successJsonStr = "{\"result\":{\"results\":[{\"E_Pic_URL\":\"\",\"E_Geo\":\"\",\"E_Info\":\"\",\"E_no\":\"1\",\"E_Category\":\"\",\"E_Name\":\"\",\"E_Memo\":\"\",\"_id\":1,\"E_URL\":\"\"},{\"E_Pic_URL\":\"\",\"E_Geo\":\"\",\"E_Info\":\"\",\"E_no\":\"1\",\"E_Category\":\"\",\"E_Name\":\"\",\"E_Memo\":\"\",\"_id\":1,\"E_URL\":\"\"}]}}"

        val item = Gson().fromJson(successJsonStr, ResponseItem::class.java)

        `when`(apiRope.getHouseData()).thenReturn(Observable.just(item))

        viewModel.onLoadHouseData()

        val listType = object : TypeToken<ArrayList<MainHouseListItem>>() {}.type
        val listData: ArrayList<MainHouseListItem> = Gson().fromJson(item.resultJsonArray.toString(), listType)

        verify(houseDataObserver).onChanged(HouseDataResultItem(DataLoadStatus.SUCCESS, listData, null))

    }


    @Test
    fun `Init Animal Data Has Http Failed or Observable Is Null`() {
        val expectErrorType = 1
        val houseItemStr = "{\"E_Pic_URL\":\"\",\"E_Geo\":\"\",\"E_Info\":\"\",\"E_no\":\"1\",\"E_Category\":\"\",\"E_Name\":\"\",\"E_Memo\":\"\",\"_id\":1,\"E_URL\":\"\"}"
        val houseItem = Gson().fromJson(houseItemStr, MainHouseListItem::class.java)

        `when`(apiRope.getAnimalData(ArgumentMatchers.anyString())).thenReturn(Observable.error(NullPointerException()))

        viewModel.onLoadAnimalListData(houseItem)

        verify(animalListDataObserver).onChanged(AnimalListDataResultItem(DataLoadStatus.FAIL, null, houseItem, expectErrorType))
    }

    @Test
    fun `Init Animal Data Has Http Success But Json Inner Error`() {
        val successJsonStr = "{}"
        val expectErrorType = 1
        val houseItemStr = "{\"E_Pic_URL\":\"\",\"E_Geo\":\"\",\"E_Info\":\"\",\"E_no\":\"1\",\"E_Category\":\"\",\"E_Name\":\"\",\"E_Memo\":\"\",\"_id\":1,\"E_URL\":\"\"}"
        val houseItem = Gson().fromJson(houseItemStr, MainHouseListItem::class.java)

        val item = Gson().fromJson(successJsonStr, ResponseItem::class.java)

        `when`(apiRope.getAnimalData(ArgumentMatchers.anyString())).thenReturn(Observable.just(item))

        viewModel.onLoadAnimalListData(houseItem)

        verify(animalListDataObserver).onChanged(AnimalListDataResultItem(DataLoadStatus.FAIL, null, houseItem, expectErrorType))
    }

    @Test
    fun `Init Animal Data Has Http Success`() {
        val successJsonStr = "{\"result\":{\"results\":[{\"_full_count\":\"1\",\"A_Behavior\":\"\",\"A_Voice03_URL\":\"\",\"A_POIGroup\":\"\",\"rank\":0.0573088,\"A_Code\":\"Koala\",\"A_Pic04_ALT\":\"無尾熊\",\"A_Voice03_ALT\":\"\",\"A_Theme_URL\":\"\",\"A_Summary\":\"\",\"A_Update\":\"2015\\/11\\/23\",\"A_Pic02_URL\":\"http:\\/\\/www.zoo.gov.tw\\/iTAP\\/03_Animals\\/KoalaHouse\\/Koala\\/Koala_Pic02.jpg\",\"A_pdf01_ALT\":\"\",\"\uFEFFA_Name_Ch\":\"無尾熊\",\"A_Theme_Name\":\"\",\"A_pdf02_ALT\":\"\",\"A_Family\":\"無尾熊科\",\"A_Adopt\":\"\",\"A_Voice01_ALT\":\"\",\"A_Pic02_ALT\":\"無尾熊\",\"A_Vedio_URL\":\"\",\"A_Pic04_URL\":\"http:\\/\\/www.zoo.gov.tw\\/iTAP\\/03_Animals\\/KoalaHouse\\/Koala\\/Koala_Pic04.jpg\",\"A_Order\":\"有袋目\",\"A_pdf01_URL\":\"\",\"A_Voice02_ALT\":\"\",\"A_Diet\":\"\",\"A_Name_Latin\":\"Phascolarctos cinereus\",\"A_Feature\":\"\",\"A_Habitat\":\"桉樹林中。\",\"A_Phylum\":\"脊索動物門\",\"A_Class\":\"哺乳綱\",\"A_Pic03_ALT\":\"無尾熊\",\"A_AlsoKnown\":\"\",\"A_Voice02_URL\":\"\",\"A_Name_En\":\"Koala\",\"A_Pic03_URL\":\"http:\\/\\/www.zoo.gov.tw\\/iTAP\\/03_Animals\\/KoalaHouse\\/Koala\\/Koala_Pic03.jpg\",\"A_Interpretation\":\"\",\"A_Location\":\"無尾熊館\",\"A_Voice01_URL\":\"\",\"A_pdf02_URL\":\"\",\"A_CID\":\"230\",\"A_Keywords\":\"\",\"A_Pic01_URL\":\"http:\\/\\/www.zoo.gov.tw\\/iTAP\\/03_Animals\\/KoalaHouse\\/Koala\\/Koala_Pic01.jpg\",\"A_Conservation\":\"\",\"A_Pic01_ALT\":\"無尾熊\",\"_id\":306,\"A_Geo\":\"MULTIPOINT ((121.5823688 24.9983738), (121.5824439 24.998155))\",\"A_Crisis\":\"\"}]}}"
        val houseItemStr = "{\"E_Pic_URL\":\"\",\"E_Geo\":\"\",\"E_Info\":\"\",\"E_no\":\"1\",\"E_Category\":\"\",\"E_Name\":\"\",\"E_Memo\":\"\",\"_id\":1,\"E_URL\":\"\"}"
        val houseItem = Gson().fromJson(houseItemStr, MainHouseListItem::class.java)

        val item = Gson().fromJson(successJsonStr, ResponseItem::class.java)


        `when`(apiRope.getAnimalData(ArgumentMatchers.anyString())).thenReturn(Observable.just(item))

        viewModel.onLoadAnimalListData(houseItem)

        val listType = object : TypeToken<ArrayList<HouseListAnimalItem>>() {}.type
        val listData: ArrayList<HouseListAnimalItem> = Gson().fromJson(item.resultJsonArray.toString(), listType)

        verify(animalListDataObserver).onChanged(AnimalListDataResultItem(DataLoadStatus.SUCCESS, listData, houseItem, null))
    }
}