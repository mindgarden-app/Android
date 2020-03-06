package com.example.mindgarden.data.remote

import com.example.mindgarden.data.vo.*
import com.example.mindgarden.network.NetworkService
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MindgardenRemoteDataSourceImpl (private val mindgardenApiService: NetworkService) :
        MindgardenRemoteDataSource{
    override fun getDiary(
        token: String,
        diaryIdx: Int,
        onSuccess: (diaryResponse: DiaryResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .getDiary(token, diaryIdx)
            .enqueue(object : Callback<DiaryResponse>{
                override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun postDiary(
        token: String,
        diary_content: RequestBody,
        weatherIdx: Int,
        diary_img: MultipartBody.Part?,
        onSuccess: (DiaryIdx) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .postDiary(token, diary_content, weatherIdx, diary_img)
            .enqueue(object : Callback<DiaryIdx>{
                override fun onFailure(call: Call<DiaryIdx>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<DiaryIdx>, response: Response<DiaryIdx>) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
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
        mindgardenApiService
            .putDiary(token,diary_content,weatherIdx,diaryIdx,diary_img)
            .enqueue(object : Callback<PostDiaryResponse>{
                override fun onFailure(call: Call<PostDiaryResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<PostDiaryResponse>, response: Response<PostDiaryResponse>) {
                    when(response.isSuccessful){
                        true->response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun getDiaryList(
        token: String,
        date: String,
        onSuccess: (diaryResponse: DiaryListResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .getDiaryList(token, date)
            .enqueue(object: Callback<DiaryListResponse>{
                override fun onFailure(call: Call<DiaryListResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<DiaryListResponse>,
                    response: Response<DiaryListResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun deleteDiaryList(
        token: String,
        diaryIdx: Int,
        onSuccess: () -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .deleteDiaryList(token,diaryIdx)
            .enqueue(object : Callback<Unit>{
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    when(response.isSuccessful){
                        true->{}
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun postPlant(
        token: String,
        body: JsonObject,
        onSuccess: (PlantResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .postPlant(token, body)
            .enqueue(object : Callback<PlantResponse>{
                override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<PlantResponse>,
                    response: Response<PlantResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun getGarden(
        token: String,
        date: String,
        onSuccess: (GardenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .getPlant(token, date)
            .enqueue(object : Callback<GardenResponse>{
                override fun onFailure(call: Call<GardenResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<GardenResponse>,
                    response: Response<GardenResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun postRenewAccessToken(
        refreshtoken: String,
        body: JsonObject,
        onSuccess: (RenewAccessTokenResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .postRenewAccessToken(refreshtoken,body)
            .enqueue(object : Callback<RenewAccessTokenResponse>{
                override fun onFailure(call: Call<RenewAccessTokenResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<RenewAccessTokenResponse>,
                    response: Response<RenewAccessTokenResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun deleteUser(
        token: String,
        onSuccess: (DeleteUserResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .deleteUser(token)
            .enqueue(object : Callback<DeleteUserResponse>{
                override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<DeleteUserResponse>, response: Response<DeleteUserResponse>) {
                    when(response.isSuccessful){
                        true->  response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun postEmailSignUp(
        body: JsonObject,
        onSuccess: (EmailSignUpResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .postEmailSignUp(body)
            .enqueue(object : Callback<EmailSignUpResponse>{
                override fun onFailure(call: Call<EmailSignUpResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(call: Call<EmailSignUpResponse>, response: Response<EmailSignUpResponse>) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun postEmailSignIn(
        body: JsonObject,
        onSuccess: (EmailSignInResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .postEmailSignIn(body)
            .enqueue(object : Callback<EmailSignInResponse>{
                override fun onFailure(call: Call<EmailSignInResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<EmailSignInResponse>,
                    response: Response<EmailSignInResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }

    override fun getForgetPassword(
        token: String,
        onSuccess: (ForgetPasswordResponse) -> Unit,
        onFail: (errorMsg: String) -> Unit
    ) {
        mindgardenApiService
            .getForgetPassword(token)
            .enqueue(object : Callback<ForgetPasswordResponse>{
                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                    onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                ) {
                    when(response.isSuccessful){
                        true-> response.body()?.let { onSuccess(it) }
                        false-> onFail(response.errorBody().toString())
                    }
                }
            })
    }
}
