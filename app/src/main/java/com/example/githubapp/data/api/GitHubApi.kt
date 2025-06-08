package com.example.githubapp.data.api

import com.example.githubapp.data.model.GitHubRepo
import com.example.githubapp.data.model.GitHubUser
import com.example.githubapp.data.model.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String, @Query("page") page: Int): UserSearchResponse

    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): GitHubUser

    @GET("users/{username}/repos")
    suspend fun getUserRepos(@Path("username") username: String, @Query("page") page: Int): List<GitHubRepo>
}
