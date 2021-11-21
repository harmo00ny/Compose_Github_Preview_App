package com.example.compose_github_preview_app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_github_preview_app.model.Repository
import com.example.compose_github_preview_app.model.User
import com.example.compose_github_preview_app.repository.GithubRepository
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapBoth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
): ViewModel() {
    private val userName = "harmo00ny"
    val userPageUrl get() = "https://github.com/$userName"

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> get() = _user

    private var _repositories: MutableStateFlow<List<Repository>> = MutableStateFlow(listOf())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    private var _selectedIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> get() = _selectedIndex

    fun setIndex(index: Int) {
        _selectedIndex.value = index
    }

    init {
        fetchData()
    }

    fun getRepositoryPageUrl(repository: Repository): String {
        return "https://github.com/$userName/${repository.name}"
    }

    private fun fetchData() = viewModelScope.launch {
        githubRepository.getUser(userName).flatMap {
            _user.emit(it)
            githubRepository.getUserRepository(userName)
        }.mapBoth(
            success = { _repositories.emit(it) },
            failure = {}
        )
    }
}