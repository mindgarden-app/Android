package com.example.mindgarden.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.example.mindgarden.Adapter.MyListAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetDiaryResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import com.example.mindgarden.Network.PUT.PutModifyDiaryResponse
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_modify_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class ModifyDiaryActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    lateinit var iconList : List<Bitmap>
    lateinit var textList : List<String>
    lateinit var dateText : String
    lateinit var dateValue : String

    val REQUEST_CODE_SELECT_IMAGE = 1004
    val REQUEST_CODE_MODIFY_ACTIVITY = 1000

    var selectPicUri : Uri? = null
    var userIdx : Int = 0

    var weatherIdx : Int = 0
    var content : String = ""

    val choiceList = arrayOf<String>("이미지 선택", "삭제")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_diary)


        //날짜 설정
        val intent : Intent = getIntent()
        dateText = intent.getStringExtra("dateText")
        txt_date_toolbar_write_diary.setText(dateText)
        dateValue = intent.getStringExtra("dateValue")


        txt_save_toolbar.setText("완료")

        getDiaryResponse()

        btn_back_toolbar.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
       btn_save_diary_toolbar.setOnClickListener {
           //수정 API를 이용하여 서버에 등록
           //일기 쓰기 액티비티 로직과 비슷하게
           putModifyDiaryResponse()

           val intent : Intent = Intent()
           intent.putExtra("from" ,200)
           setResult(Activity.RESULT_OK, intent)
           finish()
       }

        //기분선택 팝업 띄우기
        img_mood_text_modify_diary.setOnClickListener {
            moodChoice()
        }
        txt_mood_text_modify_diary.setOnClickListener {
            moodChoice()
        }

        //갤러리 접근하여 이미지 얻어오기
        img_gallary_modify_diary.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.dialog_chice_listview, null)

            builder.setView(view)


            val listview= view.findViewById(R.id.listview_dialog_choice) as ListView
            val dialog = builder.create()

            val myAdapter = MyListAdapter(this, choiceList)

            listview.setAdapter(myAdapter)

            listview.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
                when (i) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
                    }
                    1 -> {
                        deleteImage()
                    }
                }
                dialog.dismiss()
            })
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
        }

    }



    // 통신 1. 일기 상세 조회 API를 이용하여 데이터 요청
    private fun getDiaryResponse() {
        //userIdx , date 값
        val getDiaryResponse = networkService.getDiaryResponse("application/json", SharedPreferenceController.getUserID(this), dateValue)

        getDiaryResponse.enqueue(object : Callback<GetDiaryResponse> {
            override fun onFailure(call: Call<GetDiaryResponse>, t: Throwable) {
                Log.e("일기 조희 실패", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryResponse>, response: Response<GetDiaryResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        //데이터 넣어주기
                        //date, 기분 텍스트, 아이콘, 내용, 사진(있을경우)
                        //set icon and text
                        Log.e("readdiary", response.body()!!.message)

                        //아이콘 set
                        weatherIdx  = response.body()!!.data!![0].weatherIdx
                        Log.e("w", weatherIdx.toString())

                        setIcon()

                        for(i  in 0..11 ){
                            if(weatherIdx == i){
                                btn_mood_icon_modify_diary.setImageBitmap(iconList.get(i))
                                txt_mood_text_modify_diary.setText(textList.get(i))
                            }
                        }

                        //내용 set
                        content = response.body()!!.data!![0].diary_content
                        Log.e("cotent", content)
                        edt_content_modify_diary.setText(content)


                        if(response.body()!!.data!![0].diary_img != null){
                            icn_gallary_modify_diary.visibility = View.INVISIBLE
                            Glide.with(this@ModifyDiaryActivity).load(response.body()!!.data!![0].diary_img)
                                .into(img_gallary_modify_diary)
                        }

                    }
                }


            }
        })
    }

    //통신 2. 수정 API를 이용하여 서버에 등록
    private fun putModifyDiaryResponse(){

        content = edt_content_modify_diary.text.toString()
        //타입 변환(String->RequestBody)
        val content_rb = RequestBody.create(MediaType.parse("text/plain"), content)
        val date_rb = RequestBody.create(MediaType.parse("text/plain"), dateValue.substring(0,10))
        Log.e("date", dateValue.substring(0,10))

        if(selectPicUri != null){
            val options = BitmapFactory.Options()
            val inputStream : InputStream = contentResolver.openInputStream(selectPicUri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())

            val picture_rb = MultipartBody.Part.createFormData("diary_img", File(selectPicUri.toString()).name, photoBody)


            Log.e("picture_rb", picture_rb.toString())

            val putModifyDiaryResponse = networkService.putModifyDiaryResponse( content_rb, SharedPreferenceController.getUserID(this), weatherIdx, date_rb, picture_rb)

            putModifyDiaryResponse.enqueue(object : Callback<PutModifyDiaryResponse>{
                override fun onFailure(call: Call<PutModifyDiaryResponse>, t: Throwable) {
                    Log.e("수정 실패", t.toString())
                }

                override fun onResponse(call: Call<PutModifyDiaryResponse>, response: Response<PutModifyDiaryResponse>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.status == 200) {
                            Log.e("ModifyActivity", response.body()!!.message)

                        }
                    }
                    else{
                        // 400 :
                    }
                }
            })
        }else{

            val putModifyDiaryResponse = networkService.putModifyDiaryResponse( content_rb, SharedPreferenceController.getUserID(this), weatherIdx, date_rb, null)

            putModifyDiaryResponse.enqueue(object : Callback<PutModifyDiaryResponse>{
                override fun onFailure(call: Call<PutModifyDiaryResponse>, t: Throwable) {
                    Log.e("ModifyDiary failed", t.toString())
                }

                override fun onResponse(call: Call<PutModifyDiaryResponse>, response: Response<PutModifyDiaryResponse>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.status == 200) {
                            Log.e("ModifyDiary", response.body()!!.message)
                        }
                    }
                    else{
                        Log.e("Modify fail", response.body()!!.message)
                    }
                }
            })
        }


}


    //MoodChoice액티비티 팝업
    fun moodChoice(){
        val intent : Intent = Intent(this, MoodChoiceActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_MODIFY_ACTIVITY)
    }

    //이미지 받아오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리 접근
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //gallaryImg = selectImage(data)
                data?.let {
                    selectPicUri = it.data
                    Glide.with(this).load(selectPicUri)
                        .into(img_gallary_modify_diary)
                    icn_gallary_modify_diary.visibility = View.INVISIBLE
                }
            }
        }

        //기분선택 팝업 -> this
        if(requestCode == REQUEST_CODE_MODIFY_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                weatherIdx = data!!.getIntExtra("weatherIdx", 0)
                //선택한 기분 아이콘 넣어주기
                btn_mood_icon_modify_diary.setImageBitmap(data!!.getParcelableExtra<Bitmap>("moodIcn") as Bitmap)

                //선택한 기분 텍스트 넣어주기
                txt_mood_text_modify_diary.text = data!!.getStringExtra("moodTxt")
            }
        }
    }

        //이미지 삭제
    private fun deleteImage(){
        icn_gallary_modify_diary.visibility = View.VISIBLE
        img_gallary_modify_diary.setImageBitmap(null)
    }

    //setIcon
    fun setIcon(){
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
