package com.example.mindgarden.data

import com.example.mindgarden.data.vo.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MindgardenRepository {

    //일기 상세보기
    fun getDiary(
        token: String,
        diaryIdx: Int,
        onSuccess: (diaryResponse: DiaryResponse) -> Unit,
        onFail: (errorMsg : String) -> Unit
    )

    //일기 등록
    fun postDiary(
        token: String,
        diary_content : RequestBody,
        weatherIdx: Int,
        diary_img : MultipartBody.Part?,
        onSuccess: (DiaryIdx) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //일기 수정
    fun putDiary(
        token: String,
        diary_content: RequestBody,
        weatherIdx: Int,
        diaryIdx: Int,
        diary_img: MultipartBody.Part?,
        onSuccess: (PostDiaryResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //일기 목록 조회
    fun getDiaryList(
        token: String,
        date : String,
        onSuccess: (diaryResponse: DiaryListResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )
    //일기 삭제
    fun deleteDiaryList(
        token: String,
        diaryIdx: Int,
        onSuccess: (DeleteDiaryListResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //나무심기
    fun postPlant(
        token: String,
        body: JsonObject,
        onSuccess: (PlantResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //가든
    fun getGarden(
        token: String,
        date: String,
        onSuccess: (GardenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //토큰 재발금
    fun postRenewAccessToken(
        refreshtoken : String,
        body: JsonObject,
        onSuccess: (RenewAccessTokenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //탈퇴
    fun deleteUser(
        token: String,
        onSuccess: (DeleteUserResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //이메일 회원가입
    fun postEmailSignUp(
        body: JsonObject,
        onSuccess: (EmailSignUpResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //이메일 로그인
    fun postEmailSignIn(
        body: JsonObject,
        onSuccess: (EmailSignInResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //앱 내 비밀번호
    fun getForgetPassword(
        token: String,
        onSuccess: (ForgetPasswordResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

    //아이디-비밀번호 메일보내기
    fun postEmailSendPassword(
        body: JsonObject,
        onSuccess: (EmailSendPasswordResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    )

}