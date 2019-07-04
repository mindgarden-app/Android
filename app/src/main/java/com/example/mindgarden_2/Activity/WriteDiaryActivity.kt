package com.example.mindgarden_2.Activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.style.ImageSpan
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import org.jetbrains.anko.startActivity
import com.example.mindgarden_2.R


class WriteDiaryActivity : AppCompatActivity() {

    val REQUEST_CODE_WRITE_ACTIVITY = 1000
    val REQUEST_CODE_SELECT_IMAGE = 1004
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        //뒤로가기
        btn_back_toolbar.setOnClickListener{
            super.onBackPressed()
        }

        btn_save_diary_toolbar.setOnClickListener {
            //서버에 POST : 아이콘 index, 일기 내용, 이미지
            startActivity<ReadDiaryActivity>("requestCode" to REQUEST_CODE_WRITE_ACTIVITY)

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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }


    }

    //MoodChoice액티비티 팝업
    fun moodChoice(){
        val intent : Intent = Intent(this, MoodChoiceActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_WRITE_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리 접근
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                selectImage(data)

            }
        }

        //기분선택 팝업 -> this
        if(requestCode == REQUEST_CODE_WRITE_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                //선택한 기분 이미지 넣어주기
                //선택한 기분 텍스트 넣어주기
                txt_mood_text_write_diary.text = data!!.getStringExtra("moodTxt")
            }
        }
    }

    //갤러리에서 선택된 이미지를 ImageView에 넣어주기
    private fun selectImage(data : Intent?){
        /*
        이미지를 EditText 중간중간에 삽입 -> 서버 통신이 어려울 것 같아서 바꿈
        val st_index = edt_content_write_diary.getSelectionStart()
        edt_content_write_diary.getText().insert(st_index, "****")
        val et_index = edt_content_write_diary.getSelectionEnd()
        val span = edt_content_write_diary.getText()
        val image_bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data!!.getData())

        span.setSpan(ImageSpan(image_bitmap), st_index, et_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        */
        //이미지를 글 맨 위에 삽입
    }
}
