package com.example.mindgarden.ui.diary

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.network.ApplicationController
import com.example.mindgarden.network.GET.GetDiaryResponse
import com.example.mindgarden.network.NetworkService
import com.example.mindgarden.network.PUT.PutModifyDiaryResponse
import com.example.mindgarden.R
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.data.DiaryData
import com.example.mindgarden.data.MoodChoiceData
import com.example.mindgarden.network.POST.PostWriteDiaryResponse
import com.example.mindgarden.ui.diary.ReadDiaryActivity.Companion.DIARY_IDX
import kotlinx.android.synthetic.main.activity_modify_diary.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class ModifyDiaryActivity : AppCompatActivity(), Mood, DiaryDate {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    private var diaryIdx: Int = -1

    private val MoodItemList : ArrayList<MoodChoiceData> by lazy{
        ArrayList<MoodChoiceData>()
    }
    private var diaryItemList : DiaryData? = null

    private var date : String? = null
    private var b : Bitmap? = null
    private var fileName : String? = null

    val REQUEST_CODE_SELECT_IMAGE = 1004
    val REQUEST_CODE_MODIFY_ACTIVITY = 1000

    var selectPicUri : Uri? = null
    var weatherIdx : Int = 0
    var content : String = ""
    var imgState = 0

    val choiceList = arrayOf<String>("이미지 선택", "삭제")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_diary)

        //뭐하는 코드니
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        init()

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

            //크기조절
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window.attributes)
            lp.width = 700
            val window = dialog.window
            window.attributes = lp
        }

    }


    //write, modify 통합
    //구별 : diaryIdx유뮤
    //차이점 : 툴바 글씨

    private fun init(){
        getDiaryIdx()
        modifyDiaryConfig()
        getDiaryResponse()
        setToolbarBtnText()
    }

    private fun modifyDiaryConfig(){
        showMoodChoiceActivity()
        btnBackClick()
        btnSaveClick()
    }
    private fun getDiaryIdx(){
        diaryIdx =  intent.getIntExtra(DIARY_IDX, -1)
    }

    private fun setToolbarBtnText(){
        when(diaryIdx){
            -1-> txt_save_toolbar.text = "등록"
            else-> txt_save_toolbar.text = "완료"
        }
    }

    private fun setMoodIcn(idx : Int){
        getMoodList(this, MoodItemList)
        btn_mood_icon_modify_diary.setImageBitmap(MoodItemList[idx].moodIcn)
        txt_mood_text_modify_diary.text = MoodItemList[idx].moodTxt
    }

    private fun showMoodChoiceActivity(){
        img_mood_text_modify_diary.setOnClickListener {
            chooseMood()
        }
        txt_mood_text_modify_diary.setOnClickListener {
            chooseMood()
        }
    }

    private fun btnBackClick(){
        btn_back_toolbar.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun btnSaveClick(){
        btn_save_diary_toolbar.setOnClickListener {
            when(diaryIdx){
                -1->{
                    //write Response
                    postWriteDiaryResponse()
                    Intent(this, ReadDiaryActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
                else->{
                    //modify Response
                    putModifyDiaryResponse()
                    Intent(this, ReadDiaryActivity::class.java).apply {
                        putExtra(DIARY_IDX,diaryIdx)
                        setResult(Activity.RESULT_OK,this)
                        finish()
                    }
                }
            }
        }
    }

    private fun btnImageClick(){

    }

    private fun setContents(str : String) = edt_content_modify_diary.setText(str)

    private fun setDate(d : String) {
        date = getDate(d)
        txt_date_toolbar_write_diary.text = date
    }

    // 통신 1. 일기 상세 조회 API를 이용하여 데이터 요청
      private fun getDiaryResponse() {

        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this)
        }
        val getDiaryResponse = networkService.getDiaryResponse(TokenController.getAccessToken(this), diaryIdx)

        getDiaryResponse.enqueue(object : Callback<GetDiaryResponse> {
            override fun onFailure(call: Call<GetDiaryResponse>, t: Throwable) {
                Log.e("일기 조희 실패", t.toString())
            }

            override fun onResponse(call: Call<GetDiaryResponse>, response: Response<GetDiaryResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {

                        response.body()?.let {
                            diaryItemList = it.data?.get(0)

                            diaryItemList?.let { item ->
                                setContents(item.diary_content)
                                setMoodIcn(item.weatherIdx)
                                setDate(item.date)

                                item.diary_img?.let { img ->
                                    icn_gallary_modify_diary.visibility = View.INVISIBLE
                                    setImageData(img)

                                    //이미지 상태 : 이미지가 이미 있는 경우
                                    imgState = 1
                                }
                            }
                        }

                    }
                }


            }
        })
    }

    private fun setImageData(url : String){
        val target = object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                img_gallary_modify_diary.setImageBitmap(resource)
                b = resource
            }
        }

        Glide.with(this@ModifyDiaryActivity)
            .asBitmap()
            .load(url)
            .fitCenter()
            .into<SimpleTarget<Bitmap>>(target)
    }

    private fun saveBitmapToFile(bitmap: Bitmap?, name: String): String{
        val storage : File = this.cacheDir
        fileName = "$name.jpg"
        val tempFile =  File(storage, fileName)

        try{
            tempFile.createNewFile()
            val fos  = FileOutputStream(tempFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        }catch (e: FileNotFoundException){
            Log.e("FileNotFoundException: ", e.message)
        }catch (e: IOException){
            Log.e("IOException: ", e.message)
        }

        return tempFile.absolutePath
    }

    private fun postWriteDiaryResponse(){

        if(!TokenController.isValidToken(this)){
            RenewAcessTokenController.postRenewAccessToken(this)
        }
        val contentRB = RequestBody.create(MediaType.parse("text/plain"), edt_content_modify_diary.text.toString())

        if(selectPicUri != null){
            val options = BitmapFactory.Options()
            val inputStream : InputStream = contentResolver.openInputStream(selectPicUri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())

            val picture_rb = MultipartBody.Part.createFormData("diary_img", File(selectPicUri.toString()).name, photoBody)


            val postWriteDiaryResponse = networkService.postWriteDiaryResponse( TokenController.getAccessToken(this),contentRB, weatherIdx, picture_rb)

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
                        if(response.body()!!.status == 204){
                        }
                    }
                }
            })
        }else{
            val postWriteDiaryResponse = networkService.postWriteDiaryResponse(TokenController.getAccessToken(this), contentRB, weatherIdx, null)

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
                        if(response.body()!!.status == 204){
                        }
                    }
                }
            })
        }

    }

    //통신 2. 수정 API를 이용하여 서버에 등록
    private fun putModifyDiaryResponse(){
        if(!TokenController.isValidToken(this)) RenewAcessTokenController.postRenewAccessToken(this)

        //타입 변환(String->RequestBody)
        val contentRB = RequestBody.create(MediaType.parse("text/plain"), edt_content_modify_diary.text.toString())
        val dateRB = RequestBody.create(MediaType.parse("text/plain"), date)

        when(imgState){
            0->{    //이미지 삭제할 경우
                val putModifyDiaryResponse = networkService.putModifyDiaryResponse( TokenController.getAccessToken(this), contentRB, weatherIdx, dateRB, null)

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
            1->{    //이미지 원래 있을 경우
                val file = File(fileName?.let { saveBitmapToFile(b, it) })
                val photoBody = RequestBody.create(MediaType.parse("image/jpg"), file)
                val pictureRB = MultipartBody.Part.createFormData("diary_img", fileName, photoBody)
                val putModifyDiaryResponse = networkService.putModifyDiaryResponse( TokenController.getAccessToken(this), contentRB,  weatherIdx, dateRB, pictureRB)

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

                }
            2->{    //이미지 새로 첨부할 경우
              val options = BitmapFactory.Options()
              val inputStream : InputStream = contentResolver.openInputStream(selectPicUri)
              val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
              val byteArrayOutputStream = ByteArrayOutputStream()
              bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

              val photoBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())
              val pictureRB = MultipartBody.Part.createFormData("diary_img", File(selectPicUri.toString()).name, photoBody)


              val putModifyDiaryResponse = networkService.putModifyDiaryResponse( TokenController.getAccessToken(this), contentRB,  weatherIdx, dateRB, pictureRB)

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

                      }
                  }
              })
            }
        }

    }


    //MoodChoice액티비티 팝업
    fun chooseMood(){
        val intent : Intent = Intent(this, MoodActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_MODIFY_ACTIVITY)
    }

    //이미지 받아오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리 접근
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    selectPicUri = it.data
                    Glide.with(this).load(selectPicUri)
                        .into(img_gallary_modify_diary)
                    icn_gallary_modify_diary.visibility = View.INVISIBLE
                    imgState = 2
                }
            }
        }

        //기분선택 팝업 -> this
        if(requestCode == REQUEST_CODE_MODIFY_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                weatherIdx = data!!.getIntExtra("weatherIdx", 0)
                setMoodIcn(weatherIdx)
            }
        }
    }

    //이미지 삭제
    private fun deleteImage(){
        icn_gallary_modify_diary.visibility = View.VISIBLE
        img_gallary_modify_diary.setImageBitmap(null)
        imgState = 0
    }


}