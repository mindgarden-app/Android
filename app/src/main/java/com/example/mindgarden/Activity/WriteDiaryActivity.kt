package com.example.mindgarden.Activity

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import org.jetbrains.anko.startActivity
import com.example.mindgarden.R
import android.net.Uri
import android.provider.MediaStore.Images
import android.view.View
import android.widget.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.mindgarden.Adapter.MyListAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import com.example.mindgarden.RenewAcessTokenController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_modify_diary.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivityForResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class WriteDiaryActivity : AppCompatActivity() {

    val REQUEST_CODE_WRITE_ACTIVITY = 1000
    val REQUEST_CODE_SELECT_IMAGE = 1004

    val choiceList = arrayOf<String>("이미지 선택", "삭제")

    var selectPicUri : Uri? = null
    var userIdx : Int = 0
    var weatherIdx  = 0 //날씨 인덱스
    var status : Boolean = true
    lateinit var date : String  //ReadDairy에 현재 날짜 intent

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        //오늘의 날짜
        //툴바에 들어갈 format
        val dateT  = SimpleDateFormat("YY.MM.dd. (E) ")
        val dateText = dateT.format(Date()).toString()  //intent

        var intDate = SimpleDateFormat("u")
        var date2:String=""
        when(intDate.format(Date()).toInt()){
            1->date2="Mon"
            2->date2="Tue"
            3->date2="Wed"
            4->date2="Thu"
            5->date2="Fri"
            6->date2="Sat"
            7->date2="Sun"
        }

        txt_date_toolbar_write_diary.setText(dateT.format(Date()).substring(0, 9) + " (" + date2 + ")")  //setText

        Log.e("dateText", dateText)

        //서버에 보낼 format
        val dateV  = SimpleDateFormat("YYYY-MM-dd", Locale.KOREA)
       val dateValue = dateV.format(Date()).toString()
        Log.e("dateValue" ,dateValue)

        //뒤로가기
        btn_back_toolbar.setOnClickListener{
            super.onBackPressed()
        }

        btn_save_diary_toolbar.setOnClickListener {
            //서버에 POST : 아이콘 index, 일기 내용, 이미지
            postWriteDiaryResponse()
            //이미지 있을경우 딜레이 시간 주기 : 1초
            Thread.sleep(1000)
            Log.e("postWriteDiary", "ok")
            startActivityForResult<ReadDiaryActivity>(1100, "from" to 100, "dateText" to dateText, "dateValue" to dateValue)
        }

        //기분선택 팝업 띄우기
       img_mood_text_write_diary.setOnClickListener {
           moodChoice()
       }
        txt_mood_text_write_diary.setOnClickListener {
            moodChoice()
        }

        //갤러리 접근하여 이미지 얻어오기
       img_gallary_write_diary.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.dialog_chice_listview, null)

            builder.setView(view)


            val listview= view.findViewById (R.id.listview_dialog_choice) as ListView
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

           //크기조절
           val lp = WindowManager.LayoutParams()
           lp.copyFrom(dialog.window.attributes)
           lp.width = 700
           lp.height = 400
           val window = dialog.window
           window.attributes = lp
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리 접근
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //gallaryImg = selectImage(data)
                data?.let {
                    selectPicUri = it.data
                    Glide.with(this).load(selectPicUri)
                        .into(img_gallary_write_diary)
                    icn_gallary_write_diary.visibility = View.INVISIBLE
                }
            }
        }

        //기분선택 팝업 -> this
        if(requestCode == REQUEST_CODE_WRITE_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                weatherIdx = data!!.getIntExtra("weatherIdx", 0)
                //선택한 기분 아이콘 넣어주기
                btn_mood_icon_write_diary.setImageBitmap(data!!.getParcelableExtra<Bitmap>("moodIcn") as Bitmap)

                //선택한 기분 텍스트 넣어주기
                txt_mood_text_write_diary.setTextColor(Color.parseColor("#2B2B2B"))
                txt_mood_text_write_diary.text = data!!.getStringExtra("moodTxt")
            }
        }
        if(requestCode == 1100){
            intent.putExtra("status", false)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    //MoodChoice액티비티 팝업
    fun moodChoice(){
        val intent : Intent = Intent(this, MoodChoiceActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_WRITE_ACTIVITY)
    }

    //이미지 삭제
    private fun deleteImage(){
        icn_gallary_write_diary.visibility = View.VISIBLE
        img_gallary_write_diary.setImageBitmap(null)
        selectPicUri = null
    }



    private fun postWriteDiaryResponse(){

        if(!TokenController.isValidToken(ctx)){
            RenewAcessTokenController.postRenewAccessToken(ctx)
        }
        val content = edt_content_write_diary.text.toString()
        //타입 변환(String->RequestBody)
        val content_rb = RequestBody.create(MediaType.parse("text/plain"), content)
        //val date_rb = RequestBody.create(MediaType.parse("text/plain"), simpleDateFormat.toString())

        Log.e("content" , content)
        Log.e("w",weatherIdx.toString())


       if(selectPicUri != null){
           val options = BitmapFactory.Options()
           val inputStream : InputStream = contentResolver.openInputStream(selectPicUri)
           val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
           val byteArrayOutputStream = ByteArrayOutputStream()
           bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
           val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())

           val picture_rb = MultipartBody.Part.createFormData("diary_img", File(selectPicUri.toString()).name, photoBody)


           val postWriteDiaryResponse = networkService.postWriteDiaryResponse( TokenController.getAccessToken(this),content_rb, weatherIdx, picture_rb)

           postWriteDiaryResponse.enqueue(object : Callback<PostWriteDiaryResponse>{
               override fun onFailure(call: Call<PostWriteDiaryResponse>, t: Throwable) {
                   Log.e("WriteDiary failed", t.toString())
               }

               override fun onResponse(call: Call<PostWriteDiaryResponse>, response: Response<PostWriteDiaryResponse>) {
                   if(response.isSuccessful) {
                       if (response.body()!!.status == 200) {
                           Log.e("writeDiary", response.body()!!.message)
                           status = false
                       }
                   }
                   else{
                        if(response.body()!!.status == 204){
                            status = false
                        }
                   }
               }
           })
       }else{
          // val postWriteDiaryResponse = networkService.postWriteDiaryResponse( content_rb, userIdx, weatherIdx, picture_rb)
           val postWriteDiaryResponse = networkService.postWriteDiaryResponse(TokenController.getAccessToken(this), content_rb, weatherIdx, null)

           postWriteDiaryResponse.enqueue(object : Callback<PostWriteDiaryResponse>{
               override fun onFailure(call: Call<PostWriteDiaryResponse>, t: Throwable) {
                   Log.e("WriteDiary failed", t.toString())
               }

               override fun onResponse(call: Call<PostWriteDiaryResponse>, response: Response<PostWriteDiaryResponse>) {
                   if(response.isSuccessful) {
                       if (response.body()!!.status == 200) {
                           Log.e("writeDiary", response.body()!!.message)
                           status = false

                       }
                   }
                   else{
                       if(response.body()!!.status == 204){
                           status = false
                       }
                   }
               }
           })
       }

    }

/*
     fun isValid(u_token: String, comment: String): Boolean{
        if(u_token == "")
        else if(comment == "") edt_comment_write_comment.requestFocus()
        else return true
        return false
    }

*/
}
