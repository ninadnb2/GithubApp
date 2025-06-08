package com.example.githubapp.data.model

data class GitHubRepo(
    val name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int
)