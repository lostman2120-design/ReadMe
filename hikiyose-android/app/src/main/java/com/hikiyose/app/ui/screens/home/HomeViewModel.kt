package com.hikiyose.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikiyose.app.data.MethodsData
import com.hikiyose.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val settings: SettingsRepository) : ViewModel() {

    val selectedMethodId: StateFlow<String> =
        settings.selectedMethodId.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MethodsData.default.id,
        )

    fun select(id: String) {
        viewModelScope.launch { settings.setSelectedMethod(id) }
    }
}
