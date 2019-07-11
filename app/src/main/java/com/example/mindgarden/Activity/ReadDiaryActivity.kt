package com.example.mindgarden.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import org.jetbrains.anko.startActivity
import com.example.mindgarden.R
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetDiaryResponse
import com.example.mindgarden.Network.NetworkService
import kotlinx.android.synthetic.main.activity_read_diary.*
import org.jetbrains.anko.startActivityForResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReadDiaryActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    var date : String = ""
    var from = 0

    lateinit var indexList : List<Int>
    lateinit var iconList : List<Bitmap>
    lateinit var textList : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_diary)

        //목록에서, 일기 쓰기, 일기 수정
        val intent : Intent = getIntent()
        from = intent.getIntExtra("from", 0)

        if(from == 100) {  // Write -> this
            date = intent.getStringExtra("date")
            Log.e("from WriteDairy Date : ", date)
            txt_date_toolbar_read_diary.setText(date)
        }
        else if(from == 200){  //modify -> this
            date = intent.getStringExtra("date")
            Log.e("from ModifyDairy Date : ", date)
        }
        else if(from == 300){  //diaryList -> this
            date = intent.getStringExtra("date")
            Log.e("from DairyList Date : ", date)
        }
        else{
            Log.e("ReadDiary Date : ", "no value")
        }

        //통신
        getDiaryResponse(date)

        //수정버튼 -> ModifyDiaryActivity로 넘어가기
            btn_modify_diary_toolbar.setOnClickListener {
                //date값 userIdx intent
                startActivityForResult<ModifyDiaryActivity>(1200, "date" to date)
            }


        //뒤로가기 -> DiaryListAcitivy로 이동
        btn_back_toolbar.setOnClickListener{
                setResult(Activity.RESULT_OK)
                finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if(requestCode == 1100){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    // 통신 1. 일기 상세 조회 API를 이용하여 데이터 요청
    private fun getDiaryResponse(date : String) {
        //userIdx , date 값
        val getDiaryResponse = networkService.getDiaryResponse("application/json", 7, date)

        getDiaryResponse.enqueue(object : Callback<GetDiaryResponse> {
            override fun onFailure(call: Call<GetDiaryResponse>, t: Throwable) {
                Log.e("일기 조희 실패", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryResponse>, response: Response<GetDiaryResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        //데이터 넣어주기
                        //date, 기분 텍스트, 아이콘, 내용, 사진(있을경우), 저장시간
                        //set icon and text
                        Log.e("readdiary", response.body()!!.message)

                        //아이콘 set
                        val weatherIdx : Int = response.body()!!.data!![3].weatherIdx
                        Log.e("w", weatherIdx.toString())

                        for(i  in 0..11 ){
                            if(weatherIdx == i){
                                img_mood_icon_read_diary.setImageBitmap(iconList.get(i))
                                txt_mood_text_read_diary.setText(textList.get(i))
                            }
                        }

                        //내용 set
                        val content = response.body()!!.data!![2].diary_content
                        Log.e("cotent", content)
                        txt_cotent_read_diary.setText(content)

                        //time set
                        val time = response.body()!!.data!![1].date.substring(15,22)
                        txt_time_read_diary.setText(time)

                        /*diary_img set

                         if(response.body()!!.data!![5].diary_img != null){
                            img_gallary_read_diary.visibility = View.VISIBLE
                            Glide.with(this@ReadDiaryActivity).load(response.body()!!.data!![5].diary_img)
                                .into(img_gallary_read_diary)
                        }
                         */


                    }
                }
            }
        })
    }



    //setIcon
    public fun setIcon(){
        val icn1 = drawableToBitmap(R.drawable.img_weather1_good)
        val icn2  = drawableToBitmap(R.drawable.img_weather2_excited)
        val icn3 = drawableToBitmap(R.drawable.img_weather3_soso)
        val icn4 = drawableToBitmap(R.drawable.img_weather4_bored)
        val icn5 = drawableToBitmap(R.drawable.img_weather5_funny)
        val icn6 = drawableToBitmap(R.drawable.img_weather6_rainbow)
        val icn7 = drawableToBitmap(R.drawable.img_weather7_notgood)
        val icn8 = drawableToBitmap(R.drawable.img_weather8_sad)
        val icn9 = drawableToBitmap(R.drawable.img_weather9_annoying)
        val icn10 = drawableToBitmap(R.drawable.img_weather10_lightning)
        val icn11 = drawableToBitmap(R.drawable.img_weather11_none)

        //indexList = listOf<Int>(0,1,2,3,4,5,6,7,8,9,10)
        iconList = listOf<Bitmap>(icn1, icn2, icn3, icn4, icn4, icn5, icn6, icn7, icn8, icn9, icn10, icn11)
        textList = listOf<String>("좋아요", "신나요", "그냥 그래요", "심심해요", "재미있어요", "설레요",
            "별로에요", "우울해요", "짜증나요", "화가나요", "기분없음")
    }
    private fun drawableToBitmap(icnName : Int) : Bitmap {
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

}
