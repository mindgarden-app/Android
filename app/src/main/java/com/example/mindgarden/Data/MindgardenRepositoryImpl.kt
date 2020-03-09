package com.example.mindgarden.data

import com.example.mindgarden.data.remote.MindgardenRemoteDataSource
import com.example.mindgarden.data.vo.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MindgardenRepositoryImpl(private val remoteDataSource: MindgardenRemoteDataSource):
        MindgardenRepository{
    override fun getDiary(
        token: String,
        diaryIdx: Int,
        onSuccess: (diaryResponse: DiaryResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.getDiary(token,diaryIdx, onSuccess, onFail)
    }

    override fun postDiary(
        token: String,
        diary_content: RequestBody,
        weatherIdx: Int,
        diary_img: MultipartBody.Part?,
        onSuccess: (DiaryIdx) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.postDiary(token, diary_content, weatherIdx, diary_img, onSuccess, onFail)
    }

    override fun putDiary(
        token: String,
        diary_content: RequestBody,
        weatherIdx: Int,
        diaryIdx: Int,
        diary_img: MultipartBody.Part?,
        onSuccess: (PostDiaryResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.putDiary(token,diary_content,weatherIdx, diaryIdx, diary_img, onSuccess, onFail)


    }

    override fun getDiaryList(
        token: String,
        date: String,
        onSuccess: (diaryResponse: DiaryListResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.getDiaryList(token, date, onSuccess, onFail)
    }

    override fun deleteDiaryList(
        token: String,
        diaryIdx: Int,
        onSuccess: (DeleteDiaryListResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.deleteDiaryList(token, diaryIdx, onSuccess, onFail)
    }

    override fun postPlant(
        token: String,
        body: JsonObject,
        onSuccess: (PlantResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.postPlant(token, body, onSuccess, onFail)
    }

    override fun getGarden(
        token: String,
        date: String,
        onSuccess: (GardenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.getGarden(token, date, onSuccess, onFail)
    }

    override fun postRenewAccessToken(
        refreshtoken: String,
        body: JsonObject,
        onSuccess: (RenewAccessTokenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.postRenewAccessToken(refreshtoken, body, onSuccess, onFail)
    }

    override fun deleteUser(
        token: String,
        onSuccess: (DeleteUserResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.deleteUser(token, onSuccess, onFail)
    }

    override fun postEmailSignUp(
        body: JsonObject,
        onSuccess: (EmailSignUpResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.postEmailSignUp(body, onSuccess, onFail)
    }

    override fun postEmailSignIn(
        body: JsonObject,
        onSuccess: (EmailSignInResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.postEmailSignIn(body, onSuccess, onFail)
    }

    override fun getForgetPassword(
        token: String,
        onSuccess: (ForgetPasswordResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        remoteDataSource.getForgetPassword(token, onSuccess, onFail)
    }
}