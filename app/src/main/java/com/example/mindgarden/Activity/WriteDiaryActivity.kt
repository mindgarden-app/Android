package com.example.mindgarden.Activity

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
import com.bumptech.glide.Glide
import com.example.mindgarden.Adapter.MyListAdapter
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    lateinit var selectPicUri : Uri
    var userIdx = 3 //유저 인덱스
    var weatherIdx  = 0 //날씨 인덱스


    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        //뒤로가기
        btn_back_toolbar.setOnClickListener{
            super.onBackPressed()
        }

        btn_save_diary_toolbar.setOnClickListener {
            //서버에 POST : 아이콘 index, 일기 내용, 이미지
            postWriteDiaryResponse()
            Log.e("postWriteDiary", "ok")
            startActivityForResult<ReadDiaryActivity>(1100)
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
                txt_mood_text_write_diary.text = data!!.getStringExtra("moodTxt")
            }
        }
        if(requestCode == 1100){
            setResult(Activity.RESULT_OK)
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
    }



    private fun postWriteDiaryResponse(){


        val content = edt_content_write_diary.text.toString()
        //타입 변환(String->RequestBody)
        val content_rb = RequestBody.create(MediaType.parse("text/plain"), content)
        //val date_rb = RequestBody.create(MediaType.parse("text/plain"), simpleDateFormat.toString())

        Log.e("content" , content)
        Log.e("w",weatherIdx.toString())

        val options = BitmapFactory.Options()
        val inputStream : InputStream = contentResolver.openInputStream(selectPicUri)




        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())

        val picture_rb = MultipartBody.Part.createFormData("diary_img", File(selectPicUri.toString()).name, photoBody)
        Log.e("picture_rb", picture_rb.toString())
       val postWriteDiaryResponse = networkService.postWriteDiaryResponse( content_rb, userIdx, weatherIdx, picture_rb)

        postWriteDiaryResponse.enqueue(object : Callback<PostWriteDiaryResponse>{
            override fun onFailure(call: Call<PostWriteDiaryResponse>, t: Throwable) {
                Log.e("WriteDiary failed", t.toString())
            }

            override fun onResponse(call: Call<PostWriteDiaryResponse>, response: Response<PostWriteDiaryResponse>) {
                if(response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        Log.e("writeDiary", response.body()!!.message)

                    }
                }
                else{
                    // 400 :
                }
            }
        })
    }



}
