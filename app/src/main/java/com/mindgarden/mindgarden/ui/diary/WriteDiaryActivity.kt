package com.mindgarden.mindgarden.ui.diary

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mindgarden.mindgarden.db.TokenController
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.data.MindgardenRepository
import com.mindgarden.mindgarden.data.MoodChoiceData
import com.mindgarden.mindgarden.ui.base.BaseActivity
import com.mindgarden.mindgarden.ui.diary.ReadDiaryActivity.Companion.DIARY_IDX
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.layout_data_load_fail.*
import kotlinx.android.synthetic.main.toolbar_write_diary.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import java.io.*
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WriteDiaryActivity : BaseActivity(R.layout.activity_write_diary), Mood, DiaryDate {

    private val repository : MindgardenRepository by inject()
    private val moodItemList : ArrayList<MoodChoiceData> by lazy{
        ArrayList<MoodChoiceData>()
    }

    private var date : String? = null
    private var b : Bitmap? = null
    private var us : String? = null

    private var rotatedImg : Bitmap? = null
    private var selectPicUri: Uri? = null

    private val REQUEST_CODE_SELECT_IMAGE = 1004
    private val REQUEST_CODE_MODIFY_ACTIVITY = 1000

    private var diaryIdx: Int = -1
    private var weatherIdx : Int = 10
    private var imgState = 0
    private var saveClickState = false

    private val choiceList = arrayOf("이미지 선택", "삭제")

    companion object{
        var CHECK = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        if(diaryIdx!=-1){
            loadData()
        }
    }

    //공통

    private fun init(){
        getDiaryIdx()
        setToolbarText()
        checkPermission()
        showMoodChoiceActivity()
        btnSaveClick()
        btnBackClick()
        ivGalleryClick()
    }

    private fun getDiaryIdx(){
        diaryIdx =  intent.getIntExtra(DIARY_IDX, -1)
    }

    private fun setToolbarText(){
        when(diaryIdx){
            -1-> {
                txt_save_toolbar.text = "등록"
                txt_date_toolbar_write_diary.text = getCurrentDate()
            }
            else-> {
                txt_save_toolbar.text = "완료"
            }
        }
    }


    private fun setMoodIcn(idx : Int){
        weatherIdx = idx
        getMoodList(moodItemList)
        btn_mood_icon_modify_diary.setImageResource(moodItemList[idx].moodIcn)
        txtMoodTextWrite.text = moodItemList[idx].moodTxt
    }

    private fun showMoodChoiceActivity(){
        ivMoodTextWrite.setOnClickListener {
            intentToMoodChoiceActivity()
        }
        txtMoodTextWrite.setOnClickListener {
            intentToMoodChoiceActivity()
        }
    }

    private fun showProgressBar(){
        llWriteDiary.visibility = View.GONE
        pbModifyDiary.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        pbModifyDiary.visibility = View.GONE
        llWriteDiary.visibility = View.VISIBLE
    }
    private fun btnBackClick(){
        btn_back_toolbar.setOnClickListener {
            onBackPressed()
        }
    }

    private fun postOrPut(){
        when(diaryIdx){
            -1-> {
                CHECK = true
                postDiary()
            }else-> {
                CHECK = false
                putDiary()
            }
        }
    }
    private fun btnSaveClick(){
        btn_save_diary_toolbar.setOnClickListener {
            when(saveClickState){
                true->{
                    showToast("일기를 열심히 등록 중이에요 !\n잠시만 기다려주세요 ㄴ(º皿 ºㆀ)ㄱ")
                }
                false-> {
                    isValid()
                    saveClickState = true
                }
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap?, fileName : String): String{
        val storage : File = this.cacheDir
        val name = fileName
        val tempFile =  File(storage, name)

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

    private fun stringConvertToRB(str: String?) : RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"),str)
    }


    private fun intentToMoodChoiceActivity(){
        val intent = Intent(this, MoodActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_MODIFY_ACTIVITY)
    }

    private fun ivGalleryClick(){
        ivGalleryWrite.setOnClickListener{
            showChoiceDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //갤러리 접근
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    selectPicUri = it.data
                    rotatedImg = getRotatedBitmap(it.data)
                    ivGalleryWrite.setImageBitmap(rotatedImg)
                    icnGalleryWrite.visibility = View.INVISIBLE
                    imgState = 2

                }

            }
        }

        //기분선택 팝업 -> this
        if(requestCode == REQUEST_CODE_MODIFY_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                setMoodIcn(data!!.getIntExtra("weatherIdx", 10))
            }
        }
    }

    //이미지 삭제
    private fun deleteImage(){
        showIcnGallery()
        ivGalleryWrite.setImageBitmap(null)
        imgState = 0
    }

    private fun isValid(){
        if(etContentWrite.text.toString() == ""){
            //Toast.makeText(this, "내용을 작성해주세요", Toast.LENGTH_SHORT).show()
            showToast("내용을 작성해주세요")
        }else{
            postOrPut()
        }
    }

    private fun showToast(msg : String){
        val toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)
        toastText.setText(msg)
        toastText.gravity = Gravity.CENTER
        toast.view = toastView
        toast.show()
    }

    //일기 쓰기
    private fun getCurrentDate():String{
        val f = SimpleDateFormat("yy.MM.dd. (EEE)", Locale.ENGLISH)
        return f.format(Calendar.getInstance(Locale.KOREA).time)
    }

    private fun postDiary(){
        if(selectPicUri != null){
            postDiaryImageOK()
        }else{
            postDiaryImageNull()
        }
    }

    private fun postIntent(){
        Intent(this, ReadDiaryActivity::class.java).apply {
            putExtra(DIARY_IDX,diaryIdx)
            startActivity(this)
            finish()
        }
    }

    private fun postDiaryImageOK(){
        TokenController.isValidToken(this,repository)
        val contentRB = stringConvertToRB(etContentWrite.text.toString())
        val pictureRB = convertPhotoRB()

        repository
            .postDiary(
                TokenController.getAccessToken(this),contentRB, weatherIdx, pictureRB,
                {
                    if(it.status == 200){
                        hideKeyboard()
                        showProgressBar()
                        diaryIdx = it.diaryIdx
                        postIntent()
                    }
                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    private fun postDiaryImageNull(){
        TokenController.isValidToken(this,repository)

        val contentRB = stringConvertToRB(etContentWrite.text.toString())

        repository
            .postDiary(
                TokenController.getAccessToken(this),contentRB, weatherIdx,null,
                {
                    hideKeyboard()
                    hideErrorView()
                    showProgressBar()
                    diaryIdx = it.diaryIdx
                    postIntent()
                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                })
    }

    //일기 수정
    private fun setContents(str : String) = etContentWrite.setText(str)

    private fun setDate(d : String) {
        date = getDiaryDate(d)
        txt_date_toolbar_write_diary.text = date
    }

    private fun setImageData(url : String){
        icnGalleryWrite.visibility = View.GONE
        pbImgWrite.visibility = View.VISIBLE

        Glide.with(this)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean
                ): Boolean {
                    pbImgWrite.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    pbImgWrite.visibility = View.GONE
                    ivGalleryWrite.setImageBitmap(resource)
                    b = resource
                    us = url
                    return false
                }
            }).into(ivGalleryWrite)
    }

    private fun loadData(){
        TokenController.isValidToken(this,repository)


        repository
            .getDiary(
                TokenController.getAccessToken(this), diaryIdx,
                {
                    hideErrorView()
                    it.diaryResponse[0].let {resp->
                        setContents(resp.diary_content)
                        setMoodIcn(resp.weatherIdx)
                        setDate(resp.date)
                        resp.diary_img?.let { img->
                            hideIcnGallery()
                            setImageData(img)
                            imgState = 1
                        }
                    }

                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                }
            )
    }

    private fun showErrorView(){
        llWriteDiary.visibility = View.GONE
        dataLoadFailModifyDiary.visibility = View.VISIBLE
    }

    private fun hideErrorView(){
        llWriteDiary.visibility = View.VISIBLE
        dataLoadFailModifyDiary.visibility = View.GONE
    }

    private fun btnRetryDataLoad(){
        btnRetryDataLoadFail.setOnClickListener {
            loadData()
        }
    }

    private fun hideIcnGallery(){
        icnGalleryWrite.visibility = View.INVISIBLE
    }

    private fun showIcnGallery(){
        icnGalleryWrite.visibility = View.VISIBLE
    }

    private fun putDiary(){
        when(imgState){
            0->{putDiaryImageNull()}
            1->{putDiaryImageOK()}
            2->{putDiaryImageNew()}
        }
    }

    private fun putIntent(){
        Intent(this, ReadDiaryActivity::class.java).apply {
            putExtra(DIARY_IDX,diaryIdx)
            setResult(Activity.RESULT_OK,this)
            finish()
        }
    }
    private fun putDiaryImageOK(){
        TokenController.isValidToken(this,repository)

        val contentRB = stringConvertToRB(etContentWrite.text.toString())
        val fileName =  File(us).name
        val file = File(saveBitmapToFile(b,fileName))
        val photoBody = RequestBody.create(MediaType.parse("image/jpg"), file)
        val pictureRB = MultipartBody.Part.createFormData("diary_img", fileName, photoBody)

        repository
            .putDiary(TokenController.getAccessToken(this), contentRB,  weatherIdx, diaryIdx, pictureRB,
                {
                    hideKeyboard()
                    hideErrorView()
                    showProgressBar()
                    putIntent()
                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                }
            )
    }

    private fun putDiaryImageNew(){
        TokenController.isValidToken(this,repository)
        val contentRB = stringConvertToRB(etContentWrite.text.toString())
        val pictureRB = convertPhotoRB()
        repository
            .putDiary(TokenController.getAccessToken(this), contentRB,  weatherIdx, diaryIdx, pictureRB,
                {
                    hideKeyboard()
                    hideErrorView()
                    showProgressBar()
                    putIntent()
                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                }
            )
    }

    private fun putDiaryImageNull(){
        TokenController.isValidToken(this,repository)
        val contentRB = stringConvertToRB(etContentWrite.text.toString())

        repository
            .putDiary(TokenController.getAccessToken(this), contentRB, weatherIdx, diaryIdx, null,
                {
                    hideKeyboard()
                    hideErrorView()
                    showProgressBar()
                    putIntent()
                },
                {
                    hideProgressBar()
                    showErrorView()
                    btnRetryDataLoad()
                }
            )
    }

    //dialog
    private fun showChoiceDialog(){
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_chice_listview, null)
        val listView =view.findViewById<ListView>(R.id.lvDialogChoice)
        builder.setView(view)

        val choiceListAdapter = ChoiceListAdapter(this, choiceList)

        val dialog = builder.create()

        listView.adapter = choiceListAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0-> imageChooser(REQUEST_CODE_SELECT_IMAGE)
                1-> deleteImage()
            }
            dialog.dismiss()
        }

        setDialogSize(dialog)
    }

    //hide keyboard
    private fun hideKeyboard(){
        val imm : InputMethodManager= this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etContentWrite.windowToken, 0)
    }
    //camera image
    private fun imageChooser(requestCode: Int) {
        val selectionIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        startActivityForResult(selectionIntent, requestCode)
    }

    private fun getFilePathFromUri(uri: Uri): String?{
        var path: String? = null
        val contentResolver = applicationContext.contentResolver
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToNext()
            val pathColumnIdx = cursor.getColumnIndex("_data")
            if (pathColumnIdx != -1) {
                path = cursor.getString(pathColumnIdx)
            } else {
                val idColumnIdx = cursor.getColumnIndex("document_id")
                if (idColumnIdx != -1) {
                    val documentId = cursor.getString(idColumnIdx)
                    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    val selection = "_id = ?"
                    val selectionArgs = arrayOf(documentId.split(':')[1])
                    contentResolver.query(contentUri, null, selection, selectionArgs, null)
                        ?.use { cursor2 ->
                            cursor2.moveToNext()
                            val pathColumnIdx2 = cursor2.getColumnIndex("_data")
                            if (pathColumnIdx2 != -1)
                                path = cursor2.getString(pathColumnIdx2)
                        }
                }
            }
        }
        return path
    }

     fun getRotatedBitmap(uri: Uri): Bitmap? {
        contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor?.let {
            var bitmap = BitmapFactory.decodeFileDescriptor(it)
            getFilePathFromUri(uri)?.let { path ->
                ExifInterface(path).run {
                    val orientation =
                        getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                    val degrees = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }
                    if (degrees != 0f && bitmap != null) {
                        val matrix = Matrix().apply {
                            setRotate(degrees)
                        }
                        val converted = Bitmap.createBitmap(
                            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                        )

                        if (converted != bitmap) {
                            bitmap.recycle()
                            bitmap = converted
                        }
                    }
                }
                return bitmap
            }
        }
        return null
    }

    private fun convertPhotoRB(): MultipartBody.Part?{
        val byteArrayOutputStream = ByteArrayOutputStream()
        rotatedImg?.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val photoBody = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray())

        return MultipartBody.Part.createFormData("diary_img", URLEncoder.encode(getFilePathFromUri(selectPicUri!!), "utf-8"), photoBody)
    }

    private fun setDialogSize(dialog : AlertDialog){
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        //크기조절
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window.attributes)
        lp.width = 700
        val window = dialog.window
        window.attributes = lp
    }

    //permission
    fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Tag", "권한 설정 완료")
            }  else {
                Log.d("Tag", "권한 설정 요청")
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("Tag", "onRequestPermissionsResult")
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Tag", "Permission: " + permissions[0] + "was " + grantResults[0])
        }
    }
}