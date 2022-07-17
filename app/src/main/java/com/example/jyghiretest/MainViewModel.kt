package com.example.jyghiretest

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.Empty)
    val state: StateFlow<MainState> = _state

    fun navigateToProductDetail(key: String) {
        _state.update {
            it.copy(navigateProductKey = key)
        }
    }

    fun navigated() {
        _state.update {
            it.copy(navigateProductKey = null)
        }
    }

}

data class MainState(
    val navigateProductKey: String?
) {
    companion object {
        val Empty: MainState
            get() = MainState(null)
    }
}
