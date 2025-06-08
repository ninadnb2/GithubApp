package com.example.githubapp.data.model

data class GitHubUser(
    val login: String,
    val avatar_url: String,
    val bio: String?,
    val followers: Int,
    val public_repos: Int
)