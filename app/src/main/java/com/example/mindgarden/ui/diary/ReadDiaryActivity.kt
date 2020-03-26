package com.example.mindgarden.ui.diary
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import com.example.mindgarden.R
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.data.MoodChoiceData
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.ui.main.RxEventBus
import kotlinx.android.synthetic.main.activity_read_diary.*
import kotlinx.android.synthetic.main.layout_data_load_fail.*
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
            Intent(this, WriteDiaryActivity::class.java).apply {
                putExtra(DIARY_IDX, diaryIdx)
                startActivityForResult(this, READ_DIARY_REQUEST)
            }
        }
    }

    private fun btnBackClick(){
        //뒤로가기 -> DiaryListAcitivy로 이동
        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            RxEventBus.publish("load")
            finish()
        }
    }

    private fun showIv(){
        clReadDiary.visibility = View.VISIBLE
    }

    private fun setMoodIcn(idx : Int){
        getMoodList(MoodItemList)
        icnMoodRead.setImageResource(MoodItemList[idx].moodIcn)
        txtMoodRextRead.text = MoodItemList[idx].moodTxt
    }

    private fun setContents(content : String){
        txtCotentsRead.text = content
    }

    private fun setImage(img : String){
        showIv()
        pbImgRead.visibility = View.VISIBLE
        Glide.with(this@ReadDiaryActivity)
            .load(img)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    pbImgRead.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    pbImgRead.visibility = View.GONE
                    ivGalleryRead.setImageDrawable(resource)
                    return false
                }
            })
            .into(ivGalleryRead)
    }

    private fun setDateText(date : String){
        txt_date_toolbar_read_diary.text = getDiaryDate(date)
        txtTimeRead.text = getTime(date)
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
                    if(it.status == 200){
                        setContents(it.diaryResponse[0].diary_content)
                        setMoodIcn(it.diaryResponse[0].weatherIdx)
                        setDateText(it.diaryResponse[0].date)
                        it.diaryResponse[0].diary_img?.let { img->
                            showIv()
                            setImage(img)
                        }
                    }else{
                        showErrorView()
                        btnRetryDataLoad()
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