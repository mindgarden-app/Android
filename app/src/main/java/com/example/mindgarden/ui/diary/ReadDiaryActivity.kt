package com.example.mindgarden.ui.diary
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import com.example.mindgarden.R
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.data.MoodChoiceData
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.ui.login.LoginActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_modify_diary.*
import kotlinx.android.synthetic.main.activity_read_diary.*
import kotlinx.android.synthetic.main.data_load_fail.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class ReadDiaryActivity : AppCompatActivity(), Mood, DiaryDate {

    private val MoodItemList : ArrayList<MoodChoiceData> by lazy{
        ArrayList<MoodChoiceData>()
    }

    private val repository : MindgardenRepository by inject()

    private var diaryIdx: Int = -1


    companion object{
        const val DIARY_IDX = "DIARY_IDX"
        const val READ_DIARY_REQUEST = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_read_diary)
        init()
        if(diaryIdx != -1){
           loadData()
        }else{
            Log.e("diaryIdx", "not intent")
        }

    }

    private fun init(){
        getDiaryIdx()
        readDiaryConfig()
    }

    //if getDiaryIdx가 없을시에 view 로딩이나 없음 뷰
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

    private fun showIv(){
        img_gallary_read_diary.visibility = View.VISIBLE
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
            .fitCenter()
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
                loadData()
            }
        }
    }

    private fun loadData(){
        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }

        hideErrorView()

        repository
            .getDiary(
                TokenController.getAccessToken(this), diaryIdx,
                {
                    setContents(it.diaryResponse[0].diary_content)
                    setMoodIcn(it.diaryResponse[0].weatherIdx)
                    setDateText(it.diaryResponse[0].date)
                    it.diaryResponse[0].diary_img?.let { img->
                        showIv()
                        setImage(img)
                    }
                },
                {
                    showErrorView()
                    btnRetryDataLoad()
                }
            )
    }

    private fun showErrorView(){
        llReadDiary.visibility = View.GONE
        dataLoadFailReadDiary.visibility = View.VISIBLE
    }

    private fun hideErrorView(){
        llReadDiary.visibility = View.VISIBLE
        dataLoadFailReadDiary.visibility = View.GONE
    }

    private fun btnRetryDataLoad(){
        btnRetryDataLoadFail.setOnClickListener {
            loadData()
        }
    }
}