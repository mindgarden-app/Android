package com.example.mindgarden.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetDiaryResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_modify_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyDiaryActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    lateinit var indexList : List<Int>
    lateinit var iconList : List<Bitmap>
    lateinit var textList : List<String>
    lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_diary)


        //날짜 설정
        val intent : Intent = getIntent()
        date = intent.getStringExtra("date")
        txt_date_toolbar_write_diary.setText(date)

        txt_save_toolbar.setText("완료")

        btn_back_toolbar.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
       btn_save_diary_toolbar.setOnClickListener {
           //수정 API를 이용하여 서버에 등록
           //일기 쓰기 액티비티 로직과 비슷하게
           setResult(Activity.RESULT_OK)
           finish()
       }

    }
    /*

   통신 2. 수정 API를 이용하여 서버에 등록
   */

   // 통신 1. 일기 상세 조회 API를 이용하여 데이터 요청
    private fun getDiaryResponse(date : String){
       //userIdx , date 값
       val getDiaryResponse = networkService.getDiaryResponse("application/json", 5, date)

       getDiaryResponse.enqueue(object: Callback<GetDiaryResponse> {
           override fun onFailure(call: Call<GetDiaryResponse>, t: Throwable) {
               Log.e("일기 조회 실패", t.toString())
           }

           override fun onResponse(call: Call<GetDiaryResponse>, response: Response<GetDiaryResponse>) {
               if (response.isSuccessful) {
                   if (response.body()!!.status == 200) {
                       //데이터 넣어주기

                       //set icon and text
                       val weatherIdx : Int = response.body()!!.data!![3].weatherIdx
                       Log.e("w", weatherIdx.toString())

                       for(i  in 0..11 ){
                           if(weatherIdx == i){
                               icn_gallary_modify_diary.setImageBitmap(iconList.get(i))
                               txt_mood_text_modify_diary.setText(textList.get(i))
                           }
                       }
                       //content
                       edt_content_modify_diary.setText(response.body()!!.data!![2].diary_content)

                       //img

                       //date

                   }
               }
           }
       })

    }
    //데이터 보내기
    private fun putModifyDiaryResponse(){

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
