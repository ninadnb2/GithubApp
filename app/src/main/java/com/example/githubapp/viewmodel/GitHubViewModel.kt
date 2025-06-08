package com.example.githubapp.viewmodel

import androidx.lifecycle.*
import com.example.githubapp.adapter.UserListItem
import com.example.githubapp.data.model.GitHubRepo
import com.example.githubapp.data.model.GitHubUser
import com.example.githubapp.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject constructor(private val repository: GitHubRepository) : ViewModel() {
    val users = MutableLiveData<List<UserListItem>>()
    val profile = MutableLiveData<GitHubUser>()
    val repos = MutableLiveData<List<GitHubRepo>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    val profileError = MutableLiveData<String?>()
    private var currentUserPage = 1
    private var currentQuery = ""
    private var isLastUserPage = false
    private var isUserLoadingMore = false
    private var currentRepoPage = 1
    private var isLastRepoPage = false
    private var isRepoLoadingMore = false

    fun searchUsers(query: String, page: Int = 1, loadMore: Boolean = false) {
        if (isUserLoadingMore) return

        viewModelScope.launch {
            if (!loadMore) loading.value = true else isUserLoadingMore = true

            try {
                if (!loadMore) {
                    currentQuery = query
                    currentUserPage = 1
                    isLastUserPage = false
                }

                val response = repository.searchUsers(query, currentUserPage)
                val newItems = response.items
                isLastUserPage = newItems.isEmpty()

                val currentList = users.value?.toMutableList() ?: mutableListOf()
                if (loadMore) {
                    currentList.addAll(newItems.map { UserListItem.UserItem(it) })
                } else {
                    currentList.clear()
                    currentList.addAll(newItems.map { UserListItem.UserItem(it) })
                }

                users.value = currentList
                error.value = null
                currentUserPage++
            } catch (e: Exception) {
                users.value = listOf(UserListItem.ErrorItem("No Internet Available. Check your connection"))
                error.value = e.message
            } finally {
                loading.value = false
                isUserLoadingMore = false
            }
        }
    }


    fun canLoadMoreUsers(): Boolean = !isLastUserPage && !isUserLoadingMore

    fun fetchProfile(username: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                profile.value = repository.getUserProfile(username)
                profileError.value = null
            } catch (e: Exception) {
                profileError.value = e.message
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchRepos(username: String, page: Int = 1, loadMore: Boolean = false) {
        if (isRepoLoadingMore) return

        viewModelScope.launch {
            if (!loadMore) loading.value = true else isRepoLoadingMore = true

            try {
                if (!loadMore) {
                    currentRepoPage = 1
                    isLastRepoPage = false
                }

                val newRepos = repository.getUserRepos(username, currentRepoPage)
                isLastRepoPage = newRepos.isEmpty()

                val currentList = repos.value?.toMutableList() ?: mutableListOf()
                if (loadMore) currentList.addAll(newRepos) else currentList.clear().also { currentList.addAll(newRepos) }

                repos.value = currentList
                error.value = null
                currentRepoPage++
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                loading.value = false
                isRepoLoadingMore = false
            }
        }
    }

    fun canLoadMoreRepos(): Boolean = !isLastRepoPage && !isRepoLoadingMore

    fun searchUserProfileAndRepos(username: String) {
        viewModelScope.launch {
            loading.value = true
            error.value = null
            try {
                val userProfile = repository.getUserProfile(username)
                val userRepos = repository.getUserRepos(username)
                profile.value = userProfile
                repos.value = userRepos
            } catch (e: retrofit2.HttpException) {
                error.value = if (e.code() == 404) {
                    "User not found."
                } else {
                    "Server error: ${e.message()}"
                }
            } catch (e: Exception) {
                error.value = "Something went wrong: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

}
