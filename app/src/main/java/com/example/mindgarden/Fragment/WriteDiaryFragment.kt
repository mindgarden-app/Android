package com.example.mindgarden.Fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.provider.MediaStore
import android.os.Bundle
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindgarden.Activity.MoodChoiceActivity
import org.jetbrains.anko.startActivity
import com.example.mindgarden.R
import android.widget.*
import com.example.mindgarden.Adapter.MyListAdapter
import org.jetbrains.anko.support.v4.startActivityForResult
import android.support.v4.content.CursorLoader
import com.example.mindgarden.Activity.ReadDiaryActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.fragment_write_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*

/*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
val REQUEST_CODE_WRITE_ACTIVITY = 1000
val REQUEST_CODE_SELECT_IMAGE = 1004

val choiceList = arrayOf<String>("이미지 선택", "삭제")

class WriteDiaryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //tl_main_category.visibility = View.INVISIBLE

        return inflater.inflate(R.layout.fragment_write_diary, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //뒤로가기
        btn_back_toolbar.setOnClickListener{
           // super.onBackPressed()
        }

        btn_save_diary_toolbar.setOnClickListener {
            //서버에 POST : 아이콘 index, 일기 내용, 이미지
            startActivity<ReadDiaryActivity>("requestCode" to 1000)

        }

        //기분선택 팝업 띄우기
        img_mood_text_fragment_write_diary.setOnClickListener {
            moodChoice()
        }
        txt_mood_text_fragment_write_diary.setOnClickListener {
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
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            dialog.show()
        }
    }
    //MoodChoice액티비티 팝업
    fun moodChoice(){
        startActivityForResult<MoodChoiceActivity>(REQUEST_CODE_WRITE_ACTIVITY)
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

        //이미지를 글 상단 이미지뷰에 삽입
       // val name_Str = getImageNameToUri(data!!.getData())

        //이미지를 비트맵으로 받아오기
        val contentResolver = activity!!.contentResolver
        val image_bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data!!.data)
        img_gallary_fragment_write_diary.setImageBitmap(image_bitmap)
        icn_gallary_fragment_write_diary.visibility = View.INVISIBLE

    }

    //이미지 삭제
    private fun deleteImage(){
        icn_gallary_fragment_write_diary.visibility = View.VISIBLE
        img_gallary_fragment_write_diary.setImageBitmap(null)
    }

    //이미지 파일명 가져오기
        fun getImageNameToUri(data : Uri?) : String{

        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.managedQuery(data, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor.moveToFirst()
        val imgPath = cursor.getString(column_index)
        val imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1)

        return imgName
    }

}

 */
