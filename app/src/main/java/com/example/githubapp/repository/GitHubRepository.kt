package com.example.githubapp.repository

import com.example.githubapp.data.api.GitHubApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(
    private val api: GitHubApi
) {
    suspend fun searchUsers(query: String, page: Int) = api.searchUsers(query, page)
    suspend fun getUserProfile(username: String) = api.getUserProfile(username)
    suspend fun getUserRepos(username: String, page: Int = 1) = api.getUserRepos(username, page)
}
