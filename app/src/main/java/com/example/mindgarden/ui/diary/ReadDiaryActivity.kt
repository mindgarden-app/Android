package com.example.mindgarden.ui.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import com.example.mindgarden.R
import android.app.Activity
import android.view.View
import com.bumptech.glide.Glide
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.network.ApplicationController
import com.example.mindgarden.network.GET.GetDiaryResponse
import com.example.mindgarden.network.NetworkService
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.data.DiaryData
import com.example.mindgarden.data.MoodChoiceData
import kotlinx.android.synthetic.main.activity_read_diary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReadDiaryActivity : AppCompatActivity(), Mood, DiaryDate {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    private val MoodItemList : ArrayList<MoodChoiceData> by lazy{
        ArrayList<MoodChoiceData>()
    }

    private var diaryItemList : DiaryData? = null
    private var diaryIdx: Int = -1

    companion object{
        const val DIARY_IDX = "DIARY_IDX"
        const val READ_DIARY_REQUEST = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_read_diary)
        init()
        getDiaryResponse()
    }

    private fun init(){
        getDiaryIdx()
        readDiaryConfig()
    }

    private fun getDiaryIdx(){
        diaryIdx =  intent.getIntExtra(DIARY_IDX, -1)
    }

    private fun readDiaryConfig(){
        btnModifyClick()
        btnBackClick()
    }

    private fun btnModifyClick(){
        btn_modify_diary_toolbar.setOnClickListener {
            Intent(this, ModifyDiaryActivity::class.java).apply {
                putExtra(DIARY_IDX, diaryIdx)
                startActivityForResult(this, READ_DIARY_REQUEST)
            }
        }
    }

    private fun btnBackClick(){
        //뒤로가기 -> DiaryListAcitivy로 이동
        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun setMoodIcn(idx : Int){
        getMoodList(this@ReadDiaryActivity,MoodItemList)
        img_mood_icon_read_diary.setImageBitmap(MoodItemList[idx].moodIcn)
        txt_mood_text_read_diary.text = MoodItemList[idx].moodTxt
    }

    private fun setContents(content : String){
        txt_cotent_read_diary.text = content
    }

    private fun setImage(img : String){
        img_gallary_read_diary.visibility = View.VISIBLE
        Glide.with(this@ReadDiaryActivity)
            .load(img)
            .into(img_gallary_read_diary)
    }

    private fun setDateText(date : String){
        txt_date_toolbar_read_diary.text = getDiaryDate(date)
        txt_time_read_diary.text = getTime(date)
    }

    //갱신
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == READ_DIARY_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                getDiaryIdx()
                getDiaryResponse()
            }
        }
    }

    // 통신 1. 일기 상세 조회 API를 이용하여 데이터 요청
    private fun getDiaryResponse() {

        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this)
        }
        //userIdx , date 값
        Log.e("diaryIdx", diaryIdx.toString())
        val getDiaryResponse = networkService.getDiaryResponse(TokenController.getAccessToken(this), diaryIdx)

        getDiaryResponse.enqueue(object : Callback<GetDiaryResponse> {
            override fun onFailure(call: Call<GetDiaryResponse>, t: Throwable) {
                Log.e("일기 조희 실패", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryResponse>, response: Response<GetDiaryResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        diaryItemList = it.data?.get(0)

                        diaryItemList?.let { item ->
                            setContents(item.diary_content)
                            setMoodIcn(item.weatherIdx)
                            setDateText(item.date)

                            item.diary_img?.let { img ->
                                setImage(img)
                            }
                        }
                    }
                }

            }
        })
    }

}