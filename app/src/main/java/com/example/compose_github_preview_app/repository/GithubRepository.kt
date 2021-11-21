package com.example.compose_github_preview_app.repository

import com.example.compose_github_preview_app.data.remote.GithubApi
import com.example.compose_github_preview_app.model.Repository
import com.example.compose_github_preview_app.model.User
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import retrofit2.Response
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubApi: GithubApi,
) {
    suspend fun getUser(user: String): Result<User?, ResponseError> =
        githubApi.getUser(user).result()

    suspend fun getUserRepository(user: String): Result<List<Repository>, ResponseError> =
        githubApi.getUserRepository(user).result()

    private fun <T> Response<T>.result(): Result<T, ResponseError> {
        return if (isSuccessful) {
            Ok(body() ?: throw IllegalStateException("response body is null"))
        } else {
            Err(
                ResponseError(
                    code = code(),
                    message = message()
                )
            )
        }
    }
}