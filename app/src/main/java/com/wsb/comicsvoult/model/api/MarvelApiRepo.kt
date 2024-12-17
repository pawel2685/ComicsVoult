package com.wsb.comicsvoult.model.api

import com.wsb.comicsvoult.model.CharactersApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarvelApiRepo(private val api: MarvelApi) {
    val characters = MutableStateFlow<NetworkResult<CharactersApiResponse>>(NetworkResult.Initial())

    fun query(query: String) {
        characters.value = NetworkResult.Loading()
        api.getCharacters(query)
            .enqueue(object : Callback<CharactersApiResponse> {
                override fun onResponse(
                    call: Call<CharactersApiResponse?>,
                    response: Response<CharactersApiResponse?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            characters.value = NetworkResult.Success(it)
                        } ?: run {
                            characters.value = NetworkResult.Error("Empty response body")
                        }
                    } else {
                        val errorMsg = "${response.code()}: ${response.message()}"
                        println("API Error: $errorMsg")
                        characters.value = NetworkResult.Error(errorMsg)
                    }
                }

                override fun onFailure(call: Call<CharactersApiResponse?>, t: Throwable) {
                    val errorMessage = t.localizedMessage ?: "Unknown error"
                    println("API Failure: $errorMessage")
                    characters.value = NetworkResult.Error(errorMessage)
                }
            })
    }

}